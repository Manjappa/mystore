package com.manjappa.store.payments;

import com.manjappa.store.entities.Cart;
import com.manjappa.store.entities.Order;
import com.manjappa.store.exceptions.CartEmptyException;
import com.manjappa.store.exceptions.CartNotFoundException;
import com.manjappa.store.repositories.CartRepository;
import com.manjappa.store.repositories.OrderRepository;
import com.manjappa.store.services.AuthService;
import com.manjappa.store.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponseDto checkout(CheckoutReqDto request) {
        Cart cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }
        Order order= Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            CheckoutSession session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());

            return new CheckoutResponseDto(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex) {
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    Order order=orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });



    }
}
