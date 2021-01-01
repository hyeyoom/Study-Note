package com.github.hyeyoom.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        final List<OrderQueryDto> result = findOrders();
        result.forEach(o -> o.setOrderItems(findOrderItems(o.getOrderId())));
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        "FROM OrderItem oi " +
                        "JOIN oi.item i " +
                        "WHERE oi.order.id = :orderId",
                OrderItemQueryDto.class
        ).setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderedDate, o.status, d.address) " +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d",
                OrderQueryDto.class
        ).getResultList();
    }

    public List<OrderQueryDto> findAllByDtoOpt() {
        final List<OrderQueryDto> result = findOrders();
        final List<Long> orderIds = toOrderIds(result);
        final Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        final List<OrderItemQueryDto> orderItems = em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        "FROM OrderItem oi " +
                        "JOIN oi.item i " +
                        "WHERE oi.order.id IN :orderIds",
                OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        final Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        final List<Long> orderIds = result.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());
        return orderIds;
    }

    public List<OrderFlatDto> findAllByDtoFlat() {
        return em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderedDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d " +
                        "JOIN o.orderItems oi " +
                        "JOIN oi.item i",
                OrderFlatDto.class
        ).getResultList();
    }
}
