package com.Surakuri.Service;

import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Exception.ProductOutOfStockException;
import com.Surakuri.Exception.ResourceNotFoundException;
import com.Surakuri.Model.dto.CheckoutRequest;
import com.Surakuri.Model.dto.OrderResponse;
import com.Surakuri.Model.entity.Other_Business_Entities.Address;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Model.entity.Payment_Orders.OrderItem;
import com.Surakuri.Model.entity.Products_Categories.ProductVariant;
import com.Surakuri.Model.entity.User_Cart.Cart;
import com.Surakuri.Model.entity.User_Cart.CartItem;
import com.Surakuri.Model.entity.User_Cart.User;
import com.Surakuri.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Address address = addressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot checkout.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderReferenceNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(PaymentOrderStatus.PENDING);
        String fullAddress = (address.getStreet() != null ? address.getStreet() : "") + ", " +
                (address.getCity() != null ? address.getCity() : "");
        order.setShippingAddressSnapshot(fullAddress);
        order.setShippingFee(new BigDecimal("150.00"));
        order.setCreatedAt(LocalDateTime.now());

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal orderTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            ProductVariant variant = cartItem.getVariant();
            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new ProductOutOfStockException(variant.getVariantName() + " is out of stock.");
            }

            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
            productVariantRepository.save(variant);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setVariant(variant);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setMrpPrice(variant.getProduct().getMrp());
            orderItem.setSellingPrice(variant.getPrice());
            BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
            orderTotal = orderTotal.add(subtotal);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(orderTotal.add(order.getShippingFee()));

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return mapToResponse(savedOrder, req.getPaymentMethod().toString());
    }

    public List<Order> findUserOrders() {
        User user = userService.findUserProfileByJwt();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    private OrderResponse mapToResponse(Order order, String paymentMethod) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getId());
        res.setOrderReference(order.getOrderReferenceNumber());
        res.setStatus(order.getStatus().toString());
        res.setTotalAmount(order.getTotalAmount());
        res.setCustomerName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
        res.setShippingAddress(order.getShippingAddressSnapshot());
        res.setPaymentMethod(paymentMethod);
        res.setOrderDate(order.getCreatedAt());
        return res;
    }
}