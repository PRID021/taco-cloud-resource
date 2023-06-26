package sia.tacocloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import sia.tacocloud.models.LoginModel;
import sia.tacocloud.models.TacoToken;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<TacoToken> login(@RequestBody LoginModel loginModel) {

        return ResponseEntity.ok(new TacoToken());
    }

}
