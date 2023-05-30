package sia.tacocloud.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.RegistrationForm;
import sia.tacocloud.models.TacoUser;
import sia.tacocloud.repositories.UserRepository;

@RequestMapping("/register")
@Slf4j
@Controller
public class RegistrationController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        TacoUser user = form.toUser(passwordEncoder);
        log.info("Saving user: " + user);
        userRepo.save(user);
        return "redirect:/login";
    }
}
