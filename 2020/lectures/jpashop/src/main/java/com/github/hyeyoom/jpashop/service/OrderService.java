package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.Delivery;
import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.domain.OrderItem;
import com.github.hyeyoom.jpashop.domain.item.Item;
import com.github.hyeyoom.jpashop.repository.ItemRepository;
import com.github.hyeyoom.jpashop.repository.MemberRepository;
import com.github.hyeyoom.jpashop.repository.OrderRepository;
import com.github.hyeyoom.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final EntityManager em;

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
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
}
