package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookRepo;
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
@RequestMapping("/library/borrows")
@RequiredArgsConstructor
class BookBorrowController {
    private final BookBorrowRepo repo;

    private final BookRepo bookRepo;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                              @SortDefault(value = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";

        final var page = repo.findAllBorrowingByQueryAndReader(query, reader, pageable);
        return new ModelAndView("/library/borrow/borrow-index", Map.of("data", page));
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')")
    public ModelAndView delete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-delete", Map.of("entity", entity));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE')")
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.isReturned()) {
            ui.toast("Item already returned").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/library/borrows");
        }

        final var book = entity.getBook();
        book.setStatus(Book.Status.AVAILABLE);
        bookRepo.save(book);

        repo.delete(entity);
        ui.toast("Borrow ticket deleted successfully").success();
        return new ModelAndView("redirect:/library/borrows");
    }

    @GetMapping("history")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView history(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                                @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";
        final var page = repo.findAllByQueryAndReader(query, reader, pageable);
        return new ModelAndView("/library/borrow/history", Map.of("data", page));
    }

    @GetMapping("history/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')")
    public ModelAndView deleteHistory(@PathVariable String id) {
        final var history = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/history-delete", Map.of("entity", history));
    }

    @PostMapping("history/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')")
    public ModelAndView deleteHistory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var history = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!history.isReturned()) {
            ui.toast("Item currently borrowed (not a history)").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/library/borrows/history");
        }
        repo.delete(history);
        ui.toast("History deleted successfully").success();
        return new ModelAndView("redirect:/library/borrows/history");
    }
}
