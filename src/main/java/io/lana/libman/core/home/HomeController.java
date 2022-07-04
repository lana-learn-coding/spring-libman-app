package io.lana.libman.core.home;

import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.user.User;
import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.support.security.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
class HomeController {
    private final AuthFacade<User> authFacade;

    private final BookInfoRepo bookInfoRepo;

    @GetMapping("/")
    public String root() {
        if (authFacade.hasAnyAuthorities(Authorities.LIBRARIAN)) {
            return "redirect:/library/dashboard";
        }
        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView home(@RequestParam(required = false) String query,
                             @PageableDefault(size = 24) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = bookInfoRepo.findAllByQuery(query, pageable);
        return new ModelAndView("/home/home", Map.of("data", page));
    }

    @GetMapping("/home/books/{id}")
    public ModelAndView book(@PathVariable String id) {
        final var reader = authFacade.isAuthenticated() ? authFacade.requirePrincipal().getReader() : null;
        final var entity = bookInfoRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/home/book", Map.of("entity", entity, "reader", Optional.ofNullable(reader)));
    }
}

