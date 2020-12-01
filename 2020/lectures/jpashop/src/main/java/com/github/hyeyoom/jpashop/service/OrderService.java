package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.Delivery;
import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.domain.OrderItem;
import com.github.hyeyoom.jpashop.domain.item.Item;
import com.github.hyeyoom.jpashop.repository.ItemRepository;
import com.github.hyeyoom.jpashop.repository.MemberRepository;
import com.github.hyeyoom.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    
    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        final Member member = memberRepository.findOne(memberId);
        final Item item = itemRepository.findOne(itemId);
        
        // 배송 정보 생성
        final Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        final OrderItem orderItem = OrderItem.of(item, item.getPrice(), count);

        // 주문 생성
        final Order order = Order.of(member, delivery, orderItem);

        //
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        final Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 검색
    public List<Order> findOrders() {
        return null;
    }
}
