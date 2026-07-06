package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.cart.AddCartItemRequest;
import com.project.HotelManagementSystem.dto.cart.CartItemResponse;
import com.project.HotelManagementSystem.dto.cart.CartResponse;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CartItemRepository;
import com.project.HotelManagementSystem.repository.CartRepository;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Cart on the JPA Cart/CartItem entities. Adding to the cart does NOT hold inventory
 * (per product decision) — availability is only enforced at checkout/webhook. When the cart
 * is read, each item's availability is re-checked live so the UI can warn before checkout.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private static final Set<BookingStatus> ACTIVE =
            Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RoomRepository roomRepository;
    private final AuthService authService;

    private Cart getOrCreateCart() {
        User user = authService.getCurrentUser();
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0.0);
            return cartRepository.save(cart);
        });
    }

    @Transactional
    public CartResponse addItem(AddCartItemRequest req) {
        if (req.getCheckIn() == null || req.getCheckOut() == null
                || !req.getCheckOut().isAfter(req.getCheckIn())) {
            throw new IllegalArgumentException("checkOut must be after checkIn");
        }
        Room room = roomRepository.findById(req.getRoomId()).orElseThrow(() ->
                new ResourceNotFoundException("room", req.getRoomId(), "id", "rooms", "Room not found"));

        int nights = (int) ChronoUnit.DAYS.between(req.getCheckIn(), req.getCheckOut());
        double pricePerNight = room.getRoomType().getPrice();

        Cart cart = getOrCreateCart();
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setRoom(room);
        item.setCheckInDate(req.getCheckIn());
        item.setCheckOutDate(req.getCheckOut());
        item.setQuantity(Math.max(1, req.getQuantity()));
        item.setPrice(pricePerNight * nights * Math.max(1, req.getQuantity()));
        item.setCurrencyType(req.getCurrency());
        cartItemRepository.save(item);

        return getCart();
    }

    @Transactional
    public CartResponse updateQuantity(Long itemId, int quantity) {
        CartItem item = ownedItem(itemId);
        int nights = (int) ChronoUnit.DAYS.between(item.getCheckInDate(), item.getCheckOutDate());
        item.setQuantity(Math.max(1, quantity));
        item.setPrice(item.getRoom().getRoomType().getPrice() * nights * Math.max(1, quantity));
        cartItemRepository.save(item);
        return getCart();
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        cartItemRepository.delete(ownedItem(itemId));
        return getCart();
    }

    public CartResponse getCart() {
        Cart cart = getOrCreateCart();
        CartResponse resp = new CartResponse();
        resp.cartId = cart.getId();
        resp.items = new ArrayList<>();

        double total = 0;
        var currencies = new java.util.HashSet<>();
        var hotels = new java.util.HashSet<>();
        var dateSpans = new java.util.HashSet<String>();

        List<CartItem> items = cart.getCartItems() == null ? List.of() : cart.getCartItems();
        for (CartItem it : items) {
            CartItemResponse r = new CartItemResponse();
            r.itemId = it.getId();
            r.roomId = it.getRoom().getId();
            r.roomName = it.getRoom().getRoomType() != null ? it.getRoom().getRoomType().getName() : null;
            r.hotelId = it.getRoom().getHotel() != null ? it.getRoom().getHotel().getId() : null;
            r.hotelName = it.getRoom().getHotel() != null ? it.getRoom().getHotel().getName() : null;
            r.checkIn = it.getCheckInDate();
            r.checkOut = it.getCheckOutDate();
            r.nights = (int) ChronoUnit.DAYS.between(it.getCheckInDate(), it.getCheckOutDate());
            r.quantity = it.getQuantity();
            r.pricePerNight = it.getRoom().getRoomType().getPrice();
            r.totalPrice = it.getPrice();
            r.currency = it.getCurrencyType();
            r.available = roomRepository.countActiveOverlaps(
                    r.roomId, it.getCheckInDate(), it.getCheckOutDate(), ACTIVE) == 0;
            resp.items.add(r);

            total += it.getPrice();
            currencies.add(it.getCurrencyType());
            if (r.hotelId != null) hotels.add(r.hotelId);
            dateSpans.add(it.getCheckInDate() + "_" + it.getCheckOutDate());
        }
        resp.grandTotal = total;
        resp.currency = currencies.size() == 1 ? (com.project.HotelManagementSystem.entity.constants.CurrencyType) currencies.iterator().next() : null;

        // Checkout readiness: v1 requires one hotel, one date span, one currency (see README).
        if (items.isEmpty())            { resp.checkoutReady = false; resp.blockReason = "Cart is empty."; }
        else if (currencies.size() > 1) { resp.checkoutReady = false; resp.blockReason = "Cart mixes currencies. One currency per checkout."; }
        else if (hotels.size() > 1)     { resp.checkoutReady = false; resp.blockReason = "Cart mixes hotels. One hotel per checkout in this version."; }
        else if (dateSpans.size() > 1)  { resp.checkoutReady = false; resp.blockReason = "Cart mixes date ranges. One stay per checkout in this version."; }
        else                            { resp.checkoutReady = true; }
        return resp;
    }

    private CartItem ownedItem(Long itemId) {
        User user = authService.getCurrentUser();
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() ->
                new ResourceNotFoundException("cartItem", itemId, "id", "cart", "Cart item not found"));
        if (item.getCart() == null || item.getCart().getUser() == null
                || !item.getCart().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("cartItem", itemId, "id", "cart", "Cart item not found");
        }
        return item;
    }
}
