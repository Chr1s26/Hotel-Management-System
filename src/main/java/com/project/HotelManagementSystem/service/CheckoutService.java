package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.checkout.CheckoutRequest;
import com.project.HotelManagementSystem.dto.checkout.CheckoutResponse;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import com.project.HotelManagementSystem.entity.constants.PaymentStatus;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.*;
import com.project.HotelManagementSystem.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Set<BookingStatus> ACTIVE =
            Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);
    private static final Set<BookingStatus> CONFIRMED_ONLY =
            Set.of(BookingStatus.CONFIRMED);

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;
    private final AuthService authService;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest req) {
        User user = authService.getCurrentUser();
        Customer customer = customerRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("customer", null, "user", "checkout", "Customer profile not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("cart", null, "user", "checkout", "Cart is empty"));

        List<CartItem> items = cart.getCartItems();
        if (items == null || items.isEmpty()) throw new IllegalStateException("Cart is empty.");

        Set<CurrencyType> currencies = new HashSet<>();
        Set<Long> hotels = new HashSet<>();
        Set<String> spans = new HashSet<>();
        for (CartItem it : items) {
            currencies.add(it.getCurrencyType());
            hotels.add(it.getRoom().getHotel().getId());
            spans.add(it.getCheckInDate() + "_" + it.getCheckOutDate());
        }
        if (currencies.size() > 1) throw new IllegalStateException("Cart mixes currencies. One currency per checkout.");
        if (hotels.size() > 1) throw new IllegalStateException("Cart mixes hotels. One hotel per checkout in this version.");
        if (spans.size() > 1) throw new IllegalStateException("Cart mixes date ranges. One stay per checkout in this version.");

        CartItem first = items.get(0);
        CurrencyType currency = first.getCurrencyType();
        Hotel hotel = first.getRoom().getHotel();
        int nights = (int) ChronoUnit.DAYS.between(first.getCheckInDate(), first.getCheckOutDate());

        double total = 0;
        Booking booking = new Booking();
        List<BookingRoom> bookingRooms = new ArrayList<>();
        for (CartItem it : items) {
            long overlaps = roomRepository.countActiveOverlaps(
                    it.getRoom().getId(), it.getCheckInDate(), it.getCheckOutDate(), ACTIVE);
            if (overlaps > 0) {
                throw new IllegalStateException(
                        "Room " + it.getRoom().getId() + " is no longer available for those dates.");
            }
            double pricePerNight = it.getRoom().getRoomType().getPrice();
            double lineTotal = pricePerNight * nights;

            BookingRoom br = new BookingRoom();
            br.setBooking(booking);
            br.setRoom(it.getRoom());
            br.setPricePerNight(pricePerNight);
            br.setNights(nights);
            br.setTotalPrice(lineTotal);
            bookingRooms.add(br);
            total += lineTotal;
        }

        BookingGuest guest = new BookingGuest();
        guest.setBooking(booking);
        guest.setFirstName(req.firstName);
        guest.setLastName(req.lastName);
        guest.setEmail(req.email);
        guest.setCountry(req.country);
        guest.setPhone(req.phone);

        BookingPreference pref = new BookingPreference();
        pref.setBooking(booking);
        pref.setSmokingPreference(req.smokingPreference);
        pref.setBedPreference(req.bedPreference);

        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCheckInDate(first.getCheckInDate());
        booking.setCheckOutDate(first.getCheckOutDate());
        booking.setTotalNights(nights);
        booking.setPaid(false);
        booking.setOriginalTotalPrice(total);
        booking.setDiscountAmount(0);
        booking.setTaxAmount(0);
        booking.setFinalTotalPrice(total);
        booking.setCurrencyType(currency);
        booking.setHotel(hotel);
        booking.setCustomer(customer);
        booking.setDescription(req.description);
        booking.setLeadGuest(guest);
        booking.setPreference(pref);
        booking.setBookingRooms(bookingRooms);
        bookingRepository.save(booking);

        long amountMinor = Math.round(total * 100);
        try {
            PaymentIntent intent = stripeService.createPaymentIntent(
                    amountMinor, currency.name(), Map.of("bookingId", String.valueOf(booking.getId())));
            booking.setPaymentIntentId(intent.getId());
            bookingRepository.save(booking);
            return new CheckoutResponse(booking.getId(), intent.getId(),
                    intent.getClientSecret(), amountMinor, currency);
        } catch (StripeException e) {
            throw new RuntimeException("Could not start payment: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void finalizeSuccessfulPayment(String paymentIntentId) {
        Booking booking = bookingRepository.findByPaymentIntentId(paymentIntentId).orElse(null);
        if (booking == null) return;
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) return; // idempotent

        for (BookingRoom br : booking.getBookingRooms()) {
            roomRepository.findLockedById(br.getRoom().getId());
            long lost = roomRepository.countOverlapsExcludingBooking(
                    br.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(),
                    CONFIRMED_ONLY, booking.getId());
            if (lost > 0) {
                booking.setBookingStatus(BookingStatus.REJECTED);
                booking.setPaid(false);
                bookingRepository.save(booking);
                try { stripeService.refundPaymentIntent(paymentIntentId); }
                catch (StripeException e) { throw new RuntimeException("Refund failed for " + paymentIntentId, e); }
                return;
            }
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setPaid(true);

        Invoice invoice = new Invoice();
        invoice.setBooking(booking);
        invoice.setHotel(booking.getHotel());
        invoice.setCustomer(booking.getCustomer());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setInvoiceNumber("INV-" + booking.getId());
        invoice.setTotalAmount(booking.getFinalTotalPrice());
        invoice.setDescription("Booking #" + booking.getId());
        invoiceRepository.save(invoice);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setInvoice(invoice);
        payment.setCustomer(booking.getCustomer());
        payment.setPayment_method("stripe");
        payment.setPayment_date(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setCurrencyType(booking.getCurrencyType());
        paymentRepository.save(payment);

        booking.setInvoice(invoice);
        booking.setPayment(payment);
        bookingRepository.save(booking);

        cartRepository.findByUser(booking.getCustomer().getUser()).ifPresent(cart -> {
            if (cart.getCartItems() != null) cart.getCartItems().clear();
            cartRepository.save(cart);
        });
    }

    @Transactional
    public void markPaymentFailed(String paymentIntentId) {
        bookingRepository.findByPaymentIntentId(paymentIntentId).ifPresent(booking -> {
            if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
                booking.setBookingStatus(BookingStatus.CANCELLED);
                booking.setPaid(false);
                bookingRepository.save(booking);
            }
        });
    }
}
