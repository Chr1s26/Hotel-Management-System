package com.project.HotelManagementSystem.controller;


import com.project.HotelManagementSystem.dto.booking.ReviewCreateDTO;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.repository.ReviewLikeRepository;
import com.project.HotelManagementSystem.repository.ReviewRepository;
import com.project.HotelManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @PostMapping("/{hotelId}")
    public ResponseEntity<Void> writeReview(@PathVariable Long hotelId, @RequestBody ReviewCreateDTO dto) {
        User user = authService.getCurrentUser();
        Customer customer = customerRepository.findByUser(user).orElseThrow();

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();

        Review review = new Review();
        review.setHotel(hotel);
        review.setCustomer(customer);
        review.setDescription(dto.getComment());
        review.setRating(dto.getRating());
        review.setReviewDate(LocalDate.now());

        reviewRepository.save(review);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/like")
    public  ResponseEntity<Integer> toggleLike(@PathVariable Long reviewId) {
        User user = authService.getCurrentUser();
        Customer customer = customerRepository.findByUser(user).orElseThrow();

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        reviewLikeRepository.findByReviewAndCustomer(review, customer)
                .ifPresentOrElse(
                        reviewLikeRepository::delete,
                        () -> reviewLikeRepository.save(
                                new ReviewLike(review, customer)
                        )
                );

        int likeCount = reviewLikeRepository.countByReview(review);

        return ResponseEntity.ok(likeCount);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        User user = authService.getCurrentUser();
        Customer customer = customerRepository.findByUser(user).orElseThrow();

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Not your review");
        }

        reviewRepository.delete(review);

        return ResponseEntity.ok().build();
    }
}