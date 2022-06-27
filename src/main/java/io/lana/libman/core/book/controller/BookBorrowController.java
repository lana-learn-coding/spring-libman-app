package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.BookBorrow;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.reader.ReaderRepo;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Validated
@Controller
@RequestMapping("/library/borrows")
@RequiredArgsConstructor
class BookBorrowController {
    private final BookBorrowRepo repo;

    private final BookRepo bookRepo;

    private final ReaderRepo readerRepo;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_CREATE')")
    public ModelAndView create() {
        final var borrow = new BookBorrow();
        borrow.setTicketId(borrow.getId());
        return new ModelAndView("/library/borrow/borrow-edit", Map.of(
                "entity", borrow,
                "edit", false
        ));
    }

    @PostMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_CREATE')")
    public ModelAndView create(@Validated @ModelAttribute("entity") BookBorrow entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        validateBorrow(entity, bindingResult);

        if (repo.existsByTicketId(entity.getTicketId())) {
            bindingResult.rejectValue("ticket", "ticket.unique", "The ticket was already taken");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/borrow/borrow-edit", Map.of(
                    "entity", entity,
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        final var book = entity.getBook();
        book.setStatus(Book.Status.BORROWED);
        bookRepo.save(book);
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        ui.toast("Borrow ticket created successfully").success();
        return new ModelAndView("redirect:/library/borrows");
    }

    @PostMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView update(@Validated @ModelAttribute("entity") BookBorrow borrow,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               @RequestHeader String referer) {
        final var entity = repo.findById(borrow.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.isReturned()) {
            ui.toast("The borrow is already returned").error();
            return new ModelAndView("redirect:/library/borrows");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/borrow/borrow-edit", Map.of(
                    "entity", borrow,
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        entity.setDueDate(borrow.getDueDate());
        entity.setNote(borrow.getNote());
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", borrow.getId());
        ui.toast("Borrow ticket updated successfully").success();
        return new ModelAndView("redirect:" + referer);
    }

    @GetMapping("{id}/return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView returnBorrow(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-return", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @PostMapping("{id}/return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView returnBorrow(@PathVariable String id, RedirectAttributes redirectAttributes,
                                     @RequestHeader String referer) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.isReturned()) {
            ui.toast("The borrow ticket is already returned").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + referer);
        }

        final var book = entity.getBook();
        book.setStatus(Book.Status.AVAILABLE);
        bookRepo.save(book);

        entity.setReturned(true);
        entity.setReturnDate(LocalDate.now());
        repo.save(entity);
        ui.toast("Borrow ticket returned successfully").success();
        return new ModelAndView("redirect:" + referer);
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
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes, @RequestHeader String referer) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.isReturned()) {
            ui.toast("Item already returned").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + referer);
        }

        final var book = entity.getBook();
        book.setStatus(Book.Status.AVAILABLE);
        bookRepo.save(book);

        repo.delete(entity);
        ui.toast("Borrow ticket deleted successfully").success();
        return new ModelAndView("redirect:" + referer);
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

    private void validateBorrow(BookBorrow borrow, BindingResult bindingResult) {
        final var book = bookRepo.findById(borrow.getBook().getId());
        if (book.isEmpty()) bindingResult.rejectValue("book", "", "Book not exist");
        book.ifPresent(b -> {
            borrow.setBook(b);
            if (b.getTicket().size() > 0) {
                bindingResult.rejectValue("book", "", "Book was borrowed");
            }
        });

        final var reader = readerRepo.findById(borrow.getReader().getId());
        if (reader.isEmpty()) bindingResult.rejectValue("reader", "", "Reader not exist");
        reader.ifPresent(r -> {
            borrow.setReader(r);
            if (r.getBorrowingBooksCount() >= r.getBorrowLimit()) {
                bindingResult.rejectValue("reader", "", "Reader has reached borrow limit (" + r.getBorrowingBooks().size() + "/" + r.getBorrowLimit() + ")");
            }
            if (r.getOverDueBooksCount() > 0) {
                bindingResult.rejectValue("reader", "", "Reader has overdue book. Not allow to borrow book");
            }
        });
    }
}
