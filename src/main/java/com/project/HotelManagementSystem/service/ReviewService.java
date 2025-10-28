package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.review.ReviewCreateDTO;
import com.project.HotelManagementSystem.dto.review.ReviewDTO;
import com.project.HotelManagementSystem.dto.review.ReviewResponse;
import com.project.HotelManagementSystem.dto.review.ReviewUpdateDTO;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public ReviewCreateDTO createReview(ReviewCreateDTO reviewCreateDTO) {
        Review review = modelMapper.map(reviewCreateDTO, Review.class);
        review.setStatus(StatusType.ACTIVE);
        review.setCreatedAt(LocalDateTime.now());
        review.setCreatedBy(authService.getCurrentUser());
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewCreateDTO.class);
    }

    public ReviewUpdateDTO updateReview(Long id, ReviewUpdateDTO reviewUpdateDTO) {
        Review reviewOp = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review",reviewUpdateDTO,"id","reviews/edit","A review with this id cannot be found"));
        Review review = modelMapper.map(reviewUpdateDTO, Review.class);
        reviewOp.setDescription(review.getDescription());
        reviewOp.setRating(review.getRating());
        reviewOp.setReviewDate(review.getReviewDate());
        reviewOp.setHotel(review.getHotel());
        reviewOp.setCustomer(review.getCustomer());
        reviewOp.setUpdatedAt(LocalDateTime.now());
        reviewOp.setUpdatedBy(authService.getCurrentUser());
        reviewOp.setStatus(StatusType.ACTIVE);
        Review savedReview = reviewRepository.save(reviewOp);
        return modelMapper.map(savedReview,ReviewUpdateDTO.class);
    }

    public void deleteReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new ResourceNotFoundException("review",review,"id","reviews","A review with this id cannot be found");
        }
        reviewRepository.deleteById(id);}

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

    public ReviewResponse findAllReviewsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        List<Review> reviews = reviewPage.getContent();
        List<ReviewDTO> reviewDTOList = reviews.stream().map(review -> modelMapper.map(review,ReviewDTO.class)).toList();
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviews(reviewDTOList);
        reviewResponse.setPageNumber(reviewPage.getNumber());
        reviewResponse.setPageSize(reviewPage.getSize());
        reviewResponse.setTotalPages(reviewPage.getTotalPages());
        reviewResponse.setTotalElements(reviewPage.getTotalElements());
        reviewResponse.setLastPage(reviewPage.isLast());
        return reviewResponse;
    }
}