package io.lana.libman.core.home;

import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.support.security.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
class HomeController {
    private final AuthFacade<UserDetails> authFacade;

    @GetMapping("/")
    public String root() {
        if (authFacade.hasAnyAuthorities(Authorities.LIBRARIAN)) {
            return "redirect:/library/dashboard";
        }
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "/home/home";
    }
}

