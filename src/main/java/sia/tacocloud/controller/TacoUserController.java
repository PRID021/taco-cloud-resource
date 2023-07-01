package sia.tacocloud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.TacoUser;
import sia.tacocloud.repositories.UserRepository;

@RequestMapping(path = "api/users", produces = "application/json")
@RestController
@Slf4j
public class TacoUserController {
    private UserRepository userRepo;

    public TacoUserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public UserDetails gTacoUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Username: " + username);
        TacoUser userDetails = userRepo.findByUsername(username);
        log.info("User details: " + userDetails);
        return userDetails;
    }
}
