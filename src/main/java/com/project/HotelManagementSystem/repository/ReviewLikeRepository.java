package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike,Long> {
    boolean existsByReviewIdAndCustomerId(Long reviewId, Long customerId);
    Optional<ReviewLike> findByReviewIdAndCustomerId(Long reviewId, Long customerId);
    long countByReviewId(Long reviewId);

    Optional<ReviewLike> findByReviewAndCustomer(Review review, Customer customer);

    int countByReview(Review review);
}
