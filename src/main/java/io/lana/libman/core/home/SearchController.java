package io.lana.libman.core.home;

import io.lana.libman.core.reader.ReaderRepo;
import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.support.security.AuthFacade;
import io.lana.libman.support.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
class SearchController {
    private final ReaderRepo readerRepo;

    private final AuthFacade<AuthUser> authFacade;

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String search, RedirectAttributes redirectAttributes) {
        if (StringUtils.isBlank(search)) {
            return "redirect:/";
        }

        if (!authFacade.hasAnyAuthorities(Authorities.LIBRARIAN)) {
            return "redirect:/home";
        }

        final var limiter = Pageable.ofSize(1);
        if (!readerRepo.findAllByQuery("%" + search + "%", limiter).getContent().isEmpty()) {
            redirectAttributes.addAttribute("query", search);
            redirectAttributes.addAttribute("email", search);
            return "redirect:/library/readers";
        }

        return "redirect:/library/books/infos";
    }
}
