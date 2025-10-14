package com.manjappa.store.services;

import com.manjappa.store.dtos.OrderDto;
import com.manjappa.store.entities.Order;
import com.manjappa.store.exceptions.OrderNotFoundException;
import com.manjappa.store.mapper.OrderMapper;
import com.manjappa.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        List<Order> orders= orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toOrderDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).
                orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don't have access to this order.");
        }
        return orderMapper.toOrderDto(order);

    }
}
