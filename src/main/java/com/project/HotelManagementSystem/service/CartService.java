//package com.project.HotelManagementSystem.service;
//
//import com.project.HotelManagementSystem.dto.cart.CartDTO;
//import com.project.HotelManagementSystem.dto.room.RoomDTO;
//import com.project.HotelManagementSystem.dto.room.RoomSimpleDTO;
//import com.project.HotelManagementSystem.entity.Cart;
//import com.project.HotelManagementSystem.entity.CartItem;
//import com.project.HotelManagementSystem.entity.Promotion;
//import com.project.HotelManagementSystem.entity.Room;
//import com.project.HotelManagementSystem.exception.ApiException;
//import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
//import com.project.HotelManagementSystem.repository.RoomRepository;
//import com.project.HotelManagementSystem.repository.document.CartItemV2Repository;
//import com.project.HotelManagementSystem.repository.document.CartV2Repository;
//import com.project.HotelManagementSystem.util.AuthUtil;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Service
//public class CartService {
//
//    @Autowired
//    private CartV2Repository cartRepository;
//    @Autowired
//    private AuthUtil authUtil;
//    @Autowired
//    private RoomRepository roomRepository;
//    @Autowired
//    private CartItemV2Repository cartItemRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public CartService(CartV2Repository cartRepository) {
//        this.cartRepository = cartRepository;
//    }
//
//    public CartDTO addRoomToCart(Long roomId, Integer quantity) {
//        Cart cart = createCart();
//
//        //exception return type pyin ynn
//        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room",cart,"id","/carts","Room not found"));
//
//        CartItem cartItem = cartItemRepository.findCartItemByRoomIdAndCartId(cart.getId(),roomId);
//
//        if(cartItem != null) {
//            throw new ApiException("Product" + room.getId() + " already exist");
//        }
//
//        if(room.getQuantity() < quantity){
//            throw new ApiException("Please, make an order of the "+ room.getId()+ "less than or equal to the quantity " + room.getQuantity()+ ".");
//        }
//
//        CartItem newCartItem = new CartItem();
//        newCartItem.setRoom(room);
//        newCartItem.setQuantity(quantity);
//        newCartItem.setCart(cart);
//
//        Promotion appliedPromo = room.getPromotions()
//                .stream()
//                .findFirst()
//                .orElse(null);
//
//        newCartItem.setPromotion(appliedPromo);
//
//        newCartItem.setPrice(room.getPrice());
//        cartItemRepository.save(newCartItem);
//
////        Reducing stocks
////        product.setQuantity(product.getQuantity() - quantity);
//
//        cart.setTotalPrice(cart.getTotalPrice() + (room.getPrice() * quantity));
//        cartRepository.save(cart);
//
//        CartDTO cartDTO = modelMapper.map(newCartItem, CartDTO.class);
//
//        List<CartItem> cartItems = cart.getCartItems();
//
//        Stream<RoomSimpleDTO> roomDTOStream = cartItems.stream().map(item ->{
//            RoomSimpleDTO map = modelMapper.map(item.getRoom(), RoomSimpleDTO.class);
//            map.setQuantity(item.getQuantity());
//            return map;
//        });
//
//        cartDTO.setRooms(roomDTOStream.toList());
//        return cartDTO;
//    }
//
//    public List<CartDTO> getAllCarts() {
//        List<Cart> carts = cartRepository.findAll();
//
//        if(carts.size() == 0) {
//            throw new ApiException("No carts found");
//        }
//
//        List<CartDTO> cartDto = carts.stream().map( cart -> {
//            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//            List<RoomSimpleDTO> rooms = cart.getCartItems().stream().map(r -> modelMapper.map(r.getRoom(), RoomSimpleDTO.class)).collect(Collectors.toList());
//            cartDTO.setRooms(rooms);
//            return cartDTO;
//        }).collect(Collectors.toList());
//        return cartDto;
//    }
//
//    public CartDTO getCart(String emailId, Long cartId) {
//        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
//        if(cart == null) {
//            throw new ResourceNotFoundException("Cart",cart,"id","/carts","Cart not found");
//        }
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//        cart.getCartItems().forEach(c -> c.getRoom().setQuantity(c.getQuantity()));
//        List<RoomSimpleDTO> roomDTOS = cart.getCartItems().stream().map(p -> modelMapper.map(p.getRoom(), RoomSimpleDTO.class)).collect(Collectors.toList());
//        cartDTO.setRooms(roomDTOS);
//        return cartDTO;
//    }
//
//    @Transactional
//    public CartDTO updateRoomQuantityInCart(Long productId, int quantity) {
//
//        String emailId = authUtil.loggedInEmail();
//        Cart userCart = cartRepository.findByEmail(emailId);
//        Long cartId = userCart.getId();
//        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",userCart,"id","/carts","Cart not found"));
//        Room room = roomRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Room",userCart,"id","/rooms","Room not found"));
//
//        if(room.getQuantity() == 0) {
//            throw new ApiException(room.getId() + " is not available");
//        }
//        if(room.getQuantity() < quantity){
//            throw new ApiException("Please, make an order of the "+ room.getId()+ "less than or equal to the quantity " + room.getQuantity()+ ".");
//        }
//
//        return null;
//    }
//
//    private Cart createCart() {
//        Cart userCart = cartRepository.findByEmail((authUtil.loggedInEmail()));
//        if(userCart != null) {
//            return userCart;
//        }
//        Cart cart = new Cart();
//        cart.setTotalPrice(0.00);
//        cart.setUser(authUtil.loggedInUser());
//        Cart newCart = cartRepository.save(cart);
//        return newCart;
//    }
//}
