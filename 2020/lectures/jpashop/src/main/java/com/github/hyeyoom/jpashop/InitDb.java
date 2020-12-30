package com.github.hyeyoom.jpashop;

import com.github.hyeyoom.jpashop.domain.*;
import com.github.hyeyoom.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            final Member m1 = createMember("userA", "서울", "1", "1111");
            em.persist(m1);

            final Book b1 = createBook("JPA1 Book", 10000, 100);
            em.persist(b1);

            final Book b2 = createBook("JPA2 Book", 20000, 100);
            em.persist(b2);

            final OrderItem oi1 = OrderItem.of(b1, 10000, 1);
            final OrderItem oi2 = OrderItem.of(b2, 20000, 2);

            final Delivery d1 = createDelivery(m1);
            final Order order = Order.of(m1, d1, oi1, oi2);
            em.persist(order);
        }

        private Delivery createDelivery(Member m1) {
            final Delivery d1 = new Delivery();
            d1.setAddress(m1.getAddress());
            return d1;
        }

        public void dbInit2() {
            final Member m1 = createMember("userB", "부산", "2", "2222");
            em.persist(m1);

            final Book b1 = createBook("Spring1 Book", 10000, 100);
            em.persist(b1);

            final Book b2 = createBook("Spring2 Book", 20000, 100);
            em.persist(b2);

            final OrderItem oi1 = OrderItem.of(b1, 20000, 3);
            final OrderItem oi2 = OrderItem.of(b2, 40000, 4);

            final Delivery d1 = createDelivery(m1);
            final Order order = Order.of(m1, d1, oi1, oi2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            final Member m1 = new Member();
            m1.setName(name);
            m1.setAddress(new Address(city, street, zipcode));
            return m1;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            final Book b1 = new Book();
            b1.setName(name);
            b1.setPrice(price);
            b1.setStockQuantity(stockQuantity);
            return b1;
        }

    }
}

