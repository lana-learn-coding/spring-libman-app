package io.lana.libman.core.home;

import io.lana.libman.support.security.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
class HomeController {
    private final AuthFacade<UserDetails> authFacade;

    @GetMapping("/")
    public String root() {
        if (authFacade.isNotAuthenticated()) {
            return "redirect:/home";
        }
        return "redirect:/library/dashboard";
    }

    @GetMapping("/home")
    public String home() {
        return "/home/home";
    }

    @GetMapping("/library/dashboard")
    public String dashboard() {
        return "/home/dashboard";
    }
}

