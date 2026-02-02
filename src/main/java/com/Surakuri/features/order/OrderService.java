package com.Surakuri.features.order;

import com.Surakuri.features.cart.CartService; // Import CartService
import com.Surakuri.features.payment.PaymentOrderStatus;
import com.Surakuri.features.product.ProductService;
import com.Surakuri.shared.exception.ProductOutOfStockException;
import com.Surakuri.shared.exception.ResourceNotFoundException;
import com.Surakuri.features.order.DTO.CheckoutRequest;
import com.Surakuri.features.order.DTO.OrderResponse;
import com.Surakuri.features.user.*;
import com.Surakuri.features.product.ProductVariant;
import com.Surakuri.features.cart.Cart;
import com.Surakuri.features.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private UserService userService; // Use UserService instead of Repository
    // Removed CartRepository and CartItemRepository
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService; // Inject CartService

    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest req) {

        User user = userService.findById(userId); // Use UserService

        // Use CartService to get the cart
        Cart cart = cartService.getCartEntityByUserId(userId);

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
            productService.saveProductVariant(variant);

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

        // Use CartService to clear the cart
        // FIX: Pass the cart object directly to use orphanRemoval
        cartService.clearCart(cart);

        return mapToResponse(savedOrder, req.getPaymentMethod().toString());
    }

    public List<OrderResponse> findUserOrders() {
        User user = userService.findUserProfileByJwt();
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        
        return orders.stream()
                .map(order -> mapToResponse(order, "COD")) // Assuming COD for now, or fetch from PaymentOrder if available
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersBySellerId(Long sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, PaymentOrderStatus newStatus, Long sellerId) {
        // SECURITY CHECK: Use the direct database query
        boolean isSellerOrder = orderRepository.existsByOrderIdAndSellerId(orderId, sellerId);

        if (!isSellerOrder) {
            // This exception will result in a 500 error by default, or a 403 if handled by a global exception handler.
            // For now, it explains why the operation failed.
            throw new RuntimeException("You are not authorized to update this order.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);
        return orderRepository.save(order);
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