package sia.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import sia.tacocloud.AppConfig;
import sia.tacocloud.data.OrderRepository;
import sia.tacocloud.models.TacoOrder;
import sia.tacocloud.utils.ConfigReader;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
    public String processOrder(@Valid TacoOrder order, BindingResult result, SessionStatus sessionStatus) {
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
}
