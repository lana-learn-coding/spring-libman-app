package io.lana.libman.core.book.controller;

import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.IncomeRepo;
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

import java.time.LocalDate;
import java.util.Map;

@Validated
@Controller
@RequestMapping("/library/history")
@RequiredArgsConstructor
class HistoryController {
    private final IncomeRepo incomeRepo;

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

    @GetMapping("income")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView income(@RequestParam(required = false) String query,
                               @RequestParam(required = false) LocalDate from,
                               @RequestParam(required = false) LocalDate to,
                               @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = incomeRepo.findAllInRangeByQuery(query, from, to, pageable);
        final var summary = incomeRepo.findSummaryInRangeByQuery(query, from, to);
        return new ModelAndView("history/income", Map.of("data", page, "summary", summary));
    }

    @GetMapping("income/{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')")
    public ModelAndView incomeDetail(@PathVariable String id, @RequestParam(required = false) String query, Pageable pageable) {
        final var entity = incomeRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = repo.findAllByIncomeIdAndQuery(entity.getId(), query, pageable);
        return new ModelAndView("history/income-detail", Map.of("data", page, "entity", entity));
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
