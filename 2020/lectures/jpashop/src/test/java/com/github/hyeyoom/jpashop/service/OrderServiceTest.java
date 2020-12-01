package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.Address;
import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.domain.OrderStatus;
import com.github.hyeyoom.jpashop.domain.item.Book;
import com.github.hyeyoom.jpashop.domain.item.Item;
import com.github.hyeyoom.jpashop.exception.NotEnoughStockException;
import com.github.hyeyoom.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception {

        // given
        final Member member = createMember();

        final Item book = createBook("Introduction to algorithm.", 10000, 10);

        final int orderCount = 2;

        // when
        final Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        final Order foundOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, foundOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        assertEquals(1, foundOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야함");
        assertEquals(10000 * orderCount, foundOrder.getTotalPrice(), "주문 가격은 가격 * 수량임");
        assertEquals(8, book.getStockQuantity(), "수량만큼 재고가 줄어들어야함");

    }

    @Test
    void 주문취소() throws Exception {

        // given
        final Member member = createMember();
        final Item book = createBook("시골쥐", 10000, 10);

        final int orderCount = 2;
        final Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        final Order foundOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, foundOrder.getStatus(), "주문 취소 시 상태는 CANCEL.");
        assertEquals(10, book.getStockQuantity(), "재고가 복구가 되어야 함.");
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {

        // given
        final Member member = createMember();
        final Item book = createBook("Introduction to algorithm.", 10000, 10);

        final int orderCount = 11;

        // when
        // then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    private Item createBook(String name, int price, int stockQuantity) {
        final Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        final Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강변", "02140"));
        em.persist(member);
        return member;
    }
}