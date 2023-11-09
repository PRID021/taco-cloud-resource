package sia.tacocloud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.TacoOrder;
import sia.tacocloud.repositories.OrderRepository;
import sia.tacocloud.service.OrderMessagingService;

@Controller
@RequestMapping(path = "/orders")
@CrossOrigin(origins = "*")
@SessionAttributes("tacoOrder")
@Slf4j
public class OrderApiController {
    private OrderRepository orderRepository;
    private OrderMessagingService messageService;

    public OrderApiController(
            OrderRepository repo,
            OrderMessagingService messService

    ) {
        this.orderRepository = repo;
        this.messageService = messService;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping()
    public String postOrder( TacoOrder order, SessionStatus sessionStatus) {
        log.info("Order submitted: {}", order);
          sessionStatus.setComplete();
        messageService.sendOrder(order);
        return "redirect:/";
        // return orderRepository.save(order);
    }
}
