package sia.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sia.tacocloud.data.OrderRepository;
import sia.tacocloud.models.TacoOrder;

@Controller
@Slf4j
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private OrderRepository orderRepository;


    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        order.setPlacedAt(Date.from(new Date().toInstant()));
        // log.info("Order submitted: {}", order);
        orderRepository.save(order);
        sessionStatus.setComplete();
        log.info("Order submitted have been saved: {}", order);
        return "redirect:/";
    }
}
