package sia.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import sia.tacocloud.AppConfig;
import sia.tacocloud.models.TacoOrder;
import sia.tacocloud.models.TacoUser;
import sia.tacocloud.repositories.OrderRepository;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Controller
@Slf4j
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@ConfigurationProperties(prefix = "taco.orders")
public class OrderController {
    private OrderRepository orderRepository;
    private int pageSize = 20;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm(TacoOrder tacoOrder, @AuthenticationPrincipal TacoUser user) {
        tacoOrder.setDeliveryStreet(user.getStreet());
        tacoOrder.setDeliveryCity(user.getCity());
        tacoOrder.setDeliveryState(user.getState());
        tacoOrder.setDeliveryZip(user.getZip());
        tacoOrder.setCcNumber(AppConfig.getCreditCard());
        tacoOrder.setDeliveryName(user.getFullname());
        return "orderForm";
    }

    /*
     * If we annotate the TacoOrder parameter with @Valid, Spring MVC will
     * automatically perform validation on the submitted TacoOrder object after
     * binding it to the submitted form data.
     * Below is the way to perform manual validation
     */
    @PostMapping
    public String processOrder(TacoOrder order, BindingResult result, SessionStatus sessionStatus) {
        log.info("order info after validation: {}", order);

        // Manually modify the TacoOrder object before validation
        String ccNumber = AppConfig.getCreditCard();
        order.setCcNumber(ccNumber);
        log.info("order info will be validation: {}", order);

        // Perform manual validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TacoOrder>> violations = validator.validate(order);

        if (!violations.isEmpty()) {
            // Handle validation errors
            for (ConstraintViolation<TacoOrder> violation : violations) {
                log.info("ConstraintViolation: {} - {} - {}", ccNumber, validator, violations);
                result.addError(
                        new FieldError("order", violation.getPropertyPath().toString(), violation.getMessage()));
            }
            return "orderForm";
        }
        // Continue with the rest of the logic
        order.setPlacedAt(Date.from(new Date().toInstant()));
        orderRepository.save(order);
        sessionStatus.setComplete();
        log.info("Order submitted have been saved: {}", order);
        return "redirect:/";
    }


    @GetMapping

    public String ordersForUser(@AuthenticationPrincipal TacoUser user, Model model) {
        Pageable pageable = PageRequest.of(0, pageSize);
        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));
        return "orderList";
    }
}
