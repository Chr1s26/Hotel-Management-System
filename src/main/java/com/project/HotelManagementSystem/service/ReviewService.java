package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.review.ReviewCreateDTO;
import com.project.HotelManagementSystem.dto.review.ReviewDTO;
import com.project.HotelManagementSystem.dto.review.ReviewResponse;
import com.project.HotelManagementSystem.dto.review.ReviewUpdateDTO;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ReviewCreateDTO createReview(ReviewCreateDTO reviewCreateDTO) {
        Review review = modelMapper.map(reviewCreateDTO, Review.class);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewCreateDTO.class);
    }

    public ReviewUpdateDTO updateReview(Long id, ReviewUpdateDTO reviewUpdateDTO) {
        Optional<Review> reviewOp = reviewRepository.findById(id);
        Review review = modelMapper.map(reviewUpdateDTO, Review.class);
        if (reviewOp.isPresent()) {
            Review updatedReview = reviewOp.get();
            updatedReview.setDescription(review.getDescription());
            updatedReview.setRating(review.getRating());
            updatedReview.setReviewDate(review.getReviewDate());
            updatedReview.setHotel(review.getHotel());
            updatedReview.setUser(review.getUser());
            Review savedReview = reviewRepository.save(updatedReview);
            return modelMapper.map(savedReview,ReviewUpdateDTO.class);
        }
        return null;
    }

    public void deleteReview(Long id) { reviewRepository.deleteById(id);}

    public ReviewDTO findReviewById(Long id) {
        Review review = this.reviewRepository.findById(id).get();
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