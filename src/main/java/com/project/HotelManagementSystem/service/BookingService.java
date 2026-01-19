package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.BookingCreateDTO;
import com.project.HotelManagementSystem.entity.Booking;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @Transactional
    public Booking createBooking(BookingCreateDTO dto) {

        List<Room> availableRooms =
                roomRepository.findAvailableRooms(
                        dto.getHotelId(),
                        dto.getRoomTypeId(),
                        dto.getCheckIn(),
                        dto.getCheckOut()
                );

        if (availableRooms.size() < dto.getNumberOfRooms()) {
            throw new RuntimeException("Not enough rooms");
        }

        List<Room> selectedRooms =
                availableRooms.subList(0, dto.getNumberOfRooms());

        double pricePerNight = selectedRooms.get(0)
                .getRoomType().getPrice();

        long nights = ChronoUnit.DAYS.between(
                dto.getCheckIn(), dto.getCheckOut());

        double total = pricePerNight * nights * dto.getNumberOfRooms();

        Optional<Customer> customerOp = customerRepository.findByUser(authService.getCurrentUser());
        if(customerOp.isEmpty()){
            throw new RuntimeException("This user is not registered as a customer");
        }

        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setCheckInDate(dto.getCheckIn());
        booking.setCheckOutDate(dto.getCheckOut());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setDescription(dto.getDescription());
        booking.setCustomer(customerOp.get());
        booking.setRooms(new HashSet<>(selectedRooms));
        booking.setTotalPrice(total);

        bookingRepository.save(booking);

        return booking;
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("booking", id, "id", "bookings/confirm", "Booking not found"));
    }
}
