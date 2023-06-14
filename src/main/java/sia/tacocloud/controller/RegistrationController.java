package sia.tacocloud.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

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
    public String registerForm(Model model) {
        if (model.containsAttribute("registrationForm") == false) {
            log.warn("registrationForm not found in model, creating new one");
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        log.warn("registrationForm: {}", model.getAttribute("registrationForm"));
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute RegistrationForm registrationForm,
            RedirectAttributes redirectAttributes) {
        TacoUser user = registrationForm.toUser(passwordEncoder);
        // check if name user is valid and already exists
        if (user.getUsername().isEmpty()) {
            registrationForm.setUserRegisterNameError("User name must not be empty");
        }
        else{
            TacoUser userExists = userRepo.findByUsername(user.getUsername());
            if (userExists != null) {
                registrationForm.setUserRegisterNameError("User already exists");
            }
        }
                
        // check password and passwordConfirm match
        if (!registrationForm.getUserRegisterPassword().equals(registrationForm.getUserRegisterPasswordConfirm())) {
            registrationForm.setUserRegisterPasswordConfirmError("Passwords do not match");
        }
        // ether password or passwordConfirm is empty
        if (StringUtils.isEmpty(registrationForm.getUserRegisterPassword())) {
            registrationForm.setUserRegisterPasswordError("Password must not be empty");
        }
        // check other fields must not be empty
        if (StringUtils.isEmpty(registrationForm.getFullname())) {
            registrationForm.setFullnameError("Full name must not be empty");
        }
        if (StringUtils.isEmpty(registrationForm.getStreet())) {
            registrationForm.setStreetError("Street must not be empty");
        }
        if (StringUtils.isEmpty(registrationForm.getCity())) {
            registrationForm.setCityError("City must not be empty");
        }
        if (StringUtils.isEmpty(registrationForm.getState())) {
            registrationForm.setStateError("State must not be empty");
        }
        if (StringUtils.isEmpty(registrationForm.getZip())) {
            registrationForm.setZipError("Zip must not be empty");
        }
        if (StringUtils.isEmpty(registrationForm.getPhone())) {
            registrationForm.setPhoneError("Phone must not be empty");
        }

        // if any error field of registrationForm is not empty, return to registration
        // using StringUtils to void NullPointerException
        if (!StringUtils.isEmpty(registrationForm.getUserRegisterNameError())
                || !StringUtils.isEmpty(registrationForm.getUserRegisterPasswordError())
                || !StringUtils.isEmpty(registrationForm.getUserRegisterPasswordConfirmError())
                || !StringUtils.isEmpty(registrationForm.getFullnameError())
                || !StringUtils.isEmpty(registrationForm.getStreetError())
                || !StringUtils.isEmpty(registrationForm.getCityError())
                || !StringUtils.isEmpty(registrationForm.getStateError())
                || !StringUtils.isEmpty(registrationForm.getZipError())
                || !StringUtils.isEmpty(registrationForm.getPhoneError())) {
            log.warn("registrationForm: {}", registrationForm);
            redirectAttributes.addFlashAttribute("registrationForm", registrationForm);
            return "redirect:/register";
        }


        log.info("Saving user: " + user);
        userRepo.save(user);
        return "redirect:/login";
    }
}
