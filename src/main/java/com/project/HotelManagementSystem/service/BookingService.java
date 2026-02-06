package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.BookingRequestDTO;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    @Transactional
    public Long createBooking(BookingRequestDTO dto) {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setDescription(dto.getDescription());

        BookingGuest guest = new BookingGuest();
        guest.setBooking(booking);
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setCountry(dto.getCountry());
        guest.setPhone(dto.getPhone());

        BookingPreference pref = new BookingPreference();
        pref.setBooking(booking);
        pref.setSmokingPreference(dto.getSmokingPreference());
        pref.setBedPreference(dto.getBedPreference());

        booking.setLeadGuest(guest);
        booking.setPreference(pref);

        bookingRepository.save(booking);
        return booking.getId();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("booking", id, "id", "bookings/confirm", "Booking not found"));
    }
}
