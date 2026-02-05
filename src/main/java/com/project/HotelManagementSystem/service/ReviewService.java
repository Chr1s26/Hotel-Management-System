package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.ReviewDTO;
import com.project.HotelManagementSystem.dto.review.ReviewCreateDTO;
import com.project.HotelManagementSystem.dto.review.ReviewResponse;
import com.project.HotelManagementSystem.dto.review.ReviewUpdateDTO;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.entity.ReviewLike;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.repository.ReviewLikeRepository;
import com.project.HotelManagementSystem.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;


    public List<ReviewDTO> getHotelReviews(Long hotelId, Long currentCustomerId) {

        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId)
                .stream()
                .map(r -> {
                    boolean likedByMe = currentCustomerId != null &&
                            reviewLikeRepository.existsByReviewIdAndCustomerId(
                                    r.getId(), currentCustomerId
                            );

                    boolean mine = currentCustomerId != null &&
                            r.getCustomer().getId().equals(currentCustomerId);

                    return new ReviewDTO(
                            r.getId(),
                            r.getCustomer().getUser().getName(),
                            r.getDescription(),
                            r.getRating(),
                            r.getLikes().size(),
                            likedByMe,
                            mine
                    );
                })
                .toList();
    }

    public void createReview(Long hotelId, Long customerId, String comment, double rating) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        Review review = new Review();
        review.setHotel(hotel);
        review.setCustomer(customer);
        review.setDescription(comment);
        review.setRating(rating);
        review.setReviewDate(LocalDate.now());

        reviewRepository.save(review);
    }

//    public ReviewUpdateDTO updateReview(Long id, ReviewUpdateDTO reviewUpdateDTO) {
//        Review reviewOp = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review",reviewUpdateDTO,"id","reviews/edit","A review with this id cannot be found"));
//        Review review = modelMapper.map(reviewUpdateDTO, Review.class);
//        reviewOp.setDescription(review.getDescription());
//        reviewOp.setRating(review.getRating());
//        reviewOp.setReviewDate(review.getReviewDate());
//        reviewOp.setHotel(review.getHotel());
//        reviewOp.setCustomer(review.getCustomer());
//        reviewOp.setUpdatedAt(LocalDateTime.now());
//        reviewOp.setUpdatedBy(authService.getCurrentUser());
//        reviewOp.setStatus(StatusType.ACTIVE);
//        Review savedReview = reviewRepository.save(reviewOp);
//        return modelMapper.map(savedReview,ReviewUpdateDTO.class);
//    }

    public void deleteReview(Long reviewId, Long customerId) {
        Review review = reviewRepository.findByIdAndCustomerId(reviewId, customerId)
                .orElseThrow(() -> new RuntimeException("Not allowed"));

        reviewRepository.delete(review);
    }

    public int toggleLike(Long reviewId, Long customerId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        reviewLikeRepository.findByReviewIdAndCustomerId(reviewId, customerId)
                .ifPresentOrElse(
                        reviewLikeRepository::delete,
                        () -> {
                            ReviewLike like = new ReviewLike();
                            like.setReview(review);
                            like.setCustomer(customer);
                            reviewLikeRepository.save(like);
                        }
                );

        return (int) reviewLikeRepository.countByReviewId(reviewId);
    }


    public ReviewDTO findReviewById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new ResourceNotFoundException("review",review,"id","reviews","A review with this id cannot be found");
        }
        return modelMapper.map(review,ReviewDTO.class);
    }

    public List<ReviewDTO> findAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(review -> modelMapper.map(review,ReviewDTO.class)).toList();
    }

//    public ReviewResponse findAllReviewsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
//        Page<Review> reviewPage = reviewRepository.findAll(pageable);
//        List<Review> reviews = reviewPage.getContent();
//        List<ReviewDTO> reviewDTOList = reviews.stream().map(review -> modelMapper.map(review,ReviewDTO.class)).toList();
//        ReviewResponse reviewResponse = new ReviewResponse();
//        reviewResponse.setReviews(reviewDTOList);
//        reviewResponse.setPageNumber(reviewPage.getNumber());
//        reviewResponse.setPageSize(reviewPage.getSize());
//        reviewResponse.setTotalPages(reviewPage.getTotalPages());
//        reviewResponse.setTotalElements(reviewPage.getTotalElements());
//        reviewResponse.setLastPage(reviewPage.isLast());
//        return reviewResponse;
//    }
}