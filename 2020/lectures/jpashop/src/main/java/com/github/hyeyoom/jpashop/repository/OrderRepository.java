package com.github.hyeyoom.jpashop.repository;

import com.github.hyeyoom.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        final List<Order> resultList = em.createQuery("" +
                "SELECT o " +
                "FROM Order o " +
                "JOIN o.member m " +
                "WHERE o.status = :status AND m.name LIKE :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
        return resultList;
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "SELECT o " +
                        "FROM Order o " +
                        "JOIN FETCH o.member m " +
                        "JOIN FETCH o.delivery d",
                Order.class
        ).getResultList();
    }

    // 가독성, 재사용성 구림, API 스펙에 fit하게 되서 침투됨.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderedDate, o.status, d.address)" +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d",
                OrderSimpleQueryDto.class
        ).getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "SELECT DISTINCT o FROM Order o " +
                        "JOIN FETCH o.member m " +
                        "JOIN FETCH o.delivery d " +
                        "JOIN FETCH o.orderItems oi " +
                        "JOIN FETCH oi.item i",
                Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "SELECT o " +
                        "FROM Order o " +
                        "JOIN FETCH o.member m " +
                        "JOIN FETCH o.delivery d",
                Order.class
        ).setFirstResult(offset).setMaxResults(limit).getResultList();
    }
}
