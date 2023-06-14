package sia.tacocloud.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import sia.tacocloud.AppConfig;
import sia.tacocloud.OrderProps;
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
    private OrderProps orderProps;

    public OrderController(OrderRepository orderRepository, OrderProps orderProps) {
        this.orderRepository = orderRepository;
        this.orderProps = orderProps;
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
        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));
        return "orderList";
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public TacoOrder putOrder(@PathVariable("orderId") String orderId, @RequestBody TacoOrder patch) {
        TacoOrder order = orderRepository.findById(orderId).get();
        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(patch.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(patch.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(patch.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(patch.getDeliveryZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
        return orderRepository.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") String orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException  e) {
            log.error("Error deleting order: {}", e.getMessage());
        }
    }
}
