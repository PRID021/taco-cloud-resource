package sia.tacocloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sia.tacocloud.models.LoginModel;

@Controller
 // Must be explicitly specified so the spring security filter chain can be mapping to perform authentication
@RequestMapping("/authenticate")
public class AuthenticationController {

    @PostMapping(value = "/authenticate")
    public String authenticate(LoginModel loginModel) {
        System.out.println(loginModel.getUser());
        System.out.println(loginModel.getPwd());
        return "redirect:/design";
    }

}
