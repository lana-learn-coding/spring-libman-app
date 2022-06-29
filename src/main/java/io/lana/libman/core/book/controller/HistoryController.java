package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.repo.BookBorrowRepo;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Validated
@Controller
@RequestMapping("/library/history")
@RequiredArgsConstructor
class HistoryController {
    private final BookBorrowRepo repo;

    private final UIFacade ui;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView history(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                                @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";
        final var page = repo.findAllByQueryAndReader(query, reader, pageable);
        return new ModelAndView("history/history", Map.of("data", page));
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')")
    public ModelAndView deleteHistory(@PathVariable String id) {
        final var history = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("history/history-delete", Map.of("entity", history));
    }


    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')")
    public ModelAndView deleteHistory(@PathVariable String id, RedirectAttributes redirectAttributes, @RequestHeader String referer) {
        final var history = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!history.isReturned()) {
            ui.toast("Item currently borrowed (not a history)").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + referer);
        }
        repo.delete(history);
        ui.toast("History deleted successfully").success();
        return new ModelAndView("redirect:" + referer);
    }
}
