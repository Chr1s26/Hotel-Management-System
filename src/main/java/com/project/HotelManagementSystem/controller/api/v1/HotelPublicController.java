package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.booking.HotelDetailPageDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.HotelDetailPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/public")
public class HotelPublicController {
    private final HotelDetailPageService hotelDetailPageService;
    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @GetMapping("/hotel/{hotelId}")
    public String hotelDetail(@PathVariable Long hotelId, @SessionAttribute(value = "search", required = false) HotelSearchDTO searchDTO, Model model) {

        User user = authService.getCurrentUser();
        Customer customer = customerRepository.findByUser(user).orElseThrow();

        HotelDetailPageDTO hotel = hotelDetailPageService.getHotelDetailPage(hotelId,customer.getId(),searchDTO.getCheckIn());
        if (searchDTO == null) {
            searchDTO = new HotelSearchDTO();
        }
        model.addAttribute("hotel", hotel);
        model.addAttribute("search", searchDTO);
        return "search/detail";
    }
}
