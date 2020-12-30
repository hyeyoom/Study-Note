package com.github.hyeyoom.jpashop.api;

import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.repository.OrderRepository;
import com.github.hyeyoom.jpashop.repository.OrderSearch;
import com.github.hyeyoom.jpashop.repository.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        final List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3() {
        final List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }

}
