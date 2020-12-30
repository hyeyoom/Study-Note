package com.github.hyeyoom.jpashop.api;

import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.repository.OrderRepository;
import com.github.hyeyoom.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * XToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        final List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders;
    }
}
