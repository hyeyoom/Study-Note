package com.github.hyeyoom.jpashop.controller;

import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.domain.Order;
import com.github.hyeyoom.jpashop.domain.item.Item;
import com.github.hyeyoom.jpashop.repository.OrderSearch;
import com.github.hyeyoom.jpashop.service.ItemService;
import com.github.hyeyoom.jpashop.service.MemberService;
import com.github.hyeyoom.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        final List<Member> members = memberService.findMembers();
        final List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(
            @RequestParam("memberId") Long memberId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("count") int count
    ) {
        final Long orderId = orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        final List<Order> orders = orderService.findAllByCriteria(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        log.info("오리둥절ㅇㅅㅇ");
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
