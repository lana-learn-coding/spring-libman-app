package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.repo.TicketRepo;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Validated
@Controller
@RequestMapping("/library/borrows/tickets")
@RequiredArgsConstructor
class TicketController {
    private final TicketRepo repo;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/ticket-detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                              @SortDefault(value = "overDuesCount", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";

        final var page = repo.findAllBorrowingByQueryAndReader(query, reader, pageable);
        return new ModelAndView("/library/borrow/ticket-index", Map.of("data", page));
    }
}
