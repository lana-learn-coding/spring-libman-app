package io.lana.libman.core.home;

import io.lana.libman.support.security.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AuthFacade<UserDetails> authFacade;

    @RequestMapping("/")
    public String root() {
        if (authFacade.isNotAuthenticated()) {
            return "redirect:/home";
        }
        return "redirect:/library/dashboard";
    }

    @RequestMapping("/home")
    public String home() {
        return "/home/home";
    }

    @RequestMapping("/library/dashboard")
    public String dashboard() {
        return "/home/dashboard";
    }
}

