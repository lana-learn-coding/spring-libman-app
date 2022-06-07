package io.lana.libman.core.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class AuthController {
    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }
}
