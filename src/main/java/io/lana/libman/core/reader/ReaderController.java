package io.lana.libman.core.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.file.ImageService;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/library/readers")
@RequiredArgsConstructor
class ReaderController {
    private final ReaderRepo repo;

    private final BookBorrowRepo borrowRepo;

    private final ImageService imageService;

    private final ObjectMapper objectMapper;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ')")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query, Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var page = StringUtils.isBlank(query)
                ? borrowRepo.findAllBorrowingByReaderId(id, pageable)
                : borrowRepo.findAllBorrowingByReaderIdAndQuery(id, "%" + query + "%", pageable);
        return new ModelAndView("/library/reader/detail", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String email, Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        email = StringUtils.isBlank(email) ? null : "%" + email + "%";

        final var page = StringUtils.isAllBlank(query, email)
                ? repo.findAll(pageable)
                : repo.findAllByQueryOrEmail(query, email, pageable);
        return new ModelAndView("/library/reader/index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_CREATE')")
    public ModelAndView create() {
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", new Reader(),
                "edit", false
        ));
    }
}
