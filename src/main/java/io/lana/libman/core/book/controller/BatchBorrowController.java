
package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.BookBorrow;
import io.lana.libman.core.book.controller.dto.BatchReturnDto;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.reader.ReaderRepo;
import io.lana.libman.support.data.IdUtils;
import io.lana.libman.support.data.IdentifiedEntity;
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

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@Controller
@RequestMapping("/library/borrows/batch")
@RequiredArgsConstructor
class BatchBorrowController {
    private final BookBorrowRepo repo;

    private final ReaderRepo readerRepo;

    private final BookRepo bookRepo;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/borrow/borrow-detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                              @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";

        final var page = readerRepo.findAllByQueryOrTicket(reader, query, pageable);
        return new ModelAndView("/library/borrow/batch-index", Map.of("data", page));
    }

    @GetMapping("return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView returnBorrow(@ModelAttribute("entity") BatchReturnDto dto, @RequestHeader String referer) {
        final var borrows = repo.findAllByIdInAndReturnedIsFalse(dto.getIds());
        var count = 0;
        var cost = 0d;
        var email = "";
        for (final var borrow : borrows) {
            if (borrow.isReturned()) {
                ui.toast("One of the ticket is already returned").error();
                return new ModelAndView("redirect:" + referer);
            }
            final var readerEmail = borrow.getReader().getAccount().getEmail();
            email = StringUtils.defaultIfBlank(email, readerEmail);
            if (!StringUtils.equals(email, readerEmail)) {
                ui.toast("Can only batch return one reader at a time").error();
                return new ModelAndView("redirect:" + referer);
            }

            count++;
            cost += borrow.getTotalCost();
        }
        return new ModelAndView("/library/borrow/batch-return", Map.of(
                "count", count,
                "email", email,
                "cost", cost,
                "ids", dto.getIds()
        ));
    }

    @PostMapping("return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView doReturnBorrow(@ModelAttribute("entity") BatchReturnDto dto, @RequestHeader String referer) {
        final var selected = repo.findAllByIdInAndReturnedIsFalse(dto.getIds());
        if (selected.isEmpty()) {
            ui.toast("Please select something").error();
            return new ModelAndView("redirect:" + referer);
        }
        final var entity = selected.stream().findFirst().map(BookBorrow::getReader).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var ticketId = IdUtils.newTimeSortableId();
        selected.forEach(borrow -> {
            if (borrow.isReturned()) return;
            if (!borrow.getReader().equals(entity)) return;
            returnBorrow(borrow, ticketId);
        });

        ui.toast("All borrow ticket returned successfully").success();
        return new ModelAndView("redirect:" + referer);
    }

    @GetMapping("{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE')")
    public ModelAndView update(@PathVariable String id, @ModelAttribute("entity") BatchReturnDto dto) {
        final var reader = readerRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var ids = dto.getIds().isEmpty() ? null : dto.getIds();
        final var query = StringUtils.isBlank(dto.getQuery()) ? null : "%" + dto.getQuery() + "%";
        final var page = repo.findAllBorrowingByReaderIdAndQueryExclude(id, query, ids);
        final var selected = repo.findAllByIdInAndReturnedIsFalse(dto.getIds());

        final var idSets = selected.stream().map(IdentifiedEntity::getId).collect(Collectors.toSet());
        dto.setIds(dto.getIds().stream().filter(idSets::contains).collect(Collectors.toList()));

        return new ModelAndView("/library/borrow/batch-edit", Map.of(
                "batch", dto,
                "reader", reader,
                "data", page,
                "selected", selected,
                "total", selected.stream().mapToDouble(BookBorrow::getTotalCost).sum()
        ));
    }

    @GetMapping("{id}/return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE') && hasAnyAuthority('ADMIN','FORCE')")
    public ModelAndView returnBorrow(@PathVariable String id) {
        final var entity = readerRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var cost = entity.getBorrowingBooks().stream().mapToDouble(BookBorrow::getTotalCost).sum();
        return new ModelAndView("/library/borrow/batch-return", Map.of(
                "count", entity.getBorrowingBooksCount(),
                "email", entity.getAccount().getEmail(),
                "cost", cost
        ));
    }

    @PostMapping("{id}/return")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_UPDATE') && hasAnyAuthority('ADMIN','FORCE')")
    public ModelAndView returnBorrow(@PathVariable String id, @RequestHeader String referer) {
        final var entity = readerRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var ticketId = IdUtils.newTimeSortableId();
        entity.getBorrowingBooks().forEach(borrow -> returnBorrow(borrow, ticketId));
        ui.toast("Borrow ticket returned successfully").success();
        return new ModelAndView("redirect:" + referer);
    }

    private void returnBorrow(BookBorrow borrow, String ticketId) {
        final var book = borrow.getBook();
        book.setStatus(Book.Status.AVAILABLE);
        bookRepo.save(book);

        borrow.setReturned(true);
        borrow.setTicketId(ticketId);
        borrow.setReturnDate(LocalDate.now());
        repo.save(borrow);
    }
}
