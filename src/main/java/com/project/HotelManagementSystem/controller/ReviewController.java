package com.project.HotelManagementSystem.controller;


import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.review.ReviewCreateDTO;
import com.project.HotelManagementSystem.dto.review.ReviewDTO;
import com.project.HotelManagementSystem.dto.review.ReviewResponse;
import com.project.HotelManagementSystem.dto.review.ReviewUpdateDTO;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.ReviewService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final HotelService hotelService;
    private final UserService userService;

    @GetMapping
    public String findAllReviews(Model model,
                                 @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                 @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                 @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                                 @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

        ReviewResponse reviewResponse = reviewService.findAllReviewsWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<ReviewDTO> reviewDTOList = reviewResponse.getReviews();
        model.addAttribute("reviews", reviewDTOList);
        model.addAttribute("response", reviewResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "reviews/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("review", new ReviewCreateDTO());
        model.addAttribute("hotels",hotelService.findAllHotels());
        model.addAttribute("users",userService.findAllUsers());
        return "reviews/create";
    }

    @PostMapping("/create")
    public String createReview(@Valid @ModelAttribute("review") ReviewCreateDTO reviewCreateDTO, BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("hotels",hotelService.findAllHotels());
            model.addAttribute("users",userService.findAllUsers());
            return "reviews/create";
        }
        reviewService.createReview(reviewCreateDTO);
        return "redirect:/reviews";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("review", reviewService.findReviewById(id));
        model.addAttribute("hotels",hotelService.findAllHotels());
        model.addAttribute("users",userService.findAllUsers());
        return "reviews/edit";
    }

    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Long id,@Valid @ModelAttribute("review") ReviewUpdateDTO reviewUpdateDTO,BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("hotels",hotelService.findAllHotels());
            model.addAttribute("users",userService.findAllUsers());
            return "reviews/edit";
        }
        reviewService.updateReview(id, reviewUpdateDTO);
        return "redirect:/reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}