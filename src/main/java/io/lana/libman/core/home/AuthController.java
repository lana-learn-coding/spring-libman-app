package io.lana.libman.core.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class AuthController {
    @GetMapping("/logout")
    public String logout() {
        return "/auth/logout";
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }
}
