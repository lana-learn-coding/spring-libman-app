
package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.repo.BookBorrowRepo;
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
@RequestMapping("/library/borrows/batch")
@RequiredArgsConstructor
class BatchBorrowController {
    private final BookBorrowRepo repo;

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

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String reader,
                              @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        reader = StringUtils.isBlank(reader) ? null : "%" + reader + "%";

        final var page = readerRepo.findAllByQueryOrTicket(reader, query, pageable);
        return new ModelAndView("/library/borrow/batch-index", Map.of("data", page));
    }
}
