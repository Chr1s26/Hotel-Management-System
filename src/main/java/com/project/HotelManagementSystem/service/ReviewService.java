package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id,Review review) {
        Optional<Review> reviewOp = reviewRepository.findById(id);
        if (reviewOp.isPresent()) {
            Review updatedReview = reviewOp.get();
            updatedReview.setDescription(review.getDescription());
            updatedReview.setRating(review.getRating());
            if(review.getReviewDate() != null) {
                updatedReview.setReviewDate(review.getReviewDate());
            }
            updatedReview.setHotel(review.getHotel());
            updatedReview.setUser(review.getUser());
            return reviewRepository.save(updatedReview);
        }
        return null;
    }

    public void deleteReview(Long id) { reviewRepository.deleteById(id);}

    public Review findReviewById(Long id) { return reviewRepository.findById(id).orElse(null);}

    public List<Review> findAllReviews() { return reviewRepository.findAll();}
}