package io.lana.libman.core.book.controller;


import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.book.support.SimpleBookDetail;
import io.lana.libman.core.file.ImageService;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Objects;

@Validated
@Controller
@RequestMapping("/library/books/books")
@RequiredArgsConstructor
class BookController {
    private final BookInfoRepo infoRepo;

    private final BookBorrowRepo borrowRepo;

    private final BookRepo repo;

    private final ImageService imageService;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/book-detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String shelfId,
                              @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        shelfId = StringUtils.trimToNull(shelfId);
        final var page = StringUtils.isAllBlank(query, shelfId)
                ? repo.findAll(pageable)
                : repo.findAllByQueryAndShelfId(query, shelfId, pageable);
        return new ModelAndView("/library/book/book-index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/book-edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_CREATE')")
    public ModelAndView create(@RequestParam(required = false) String parentId) {
        final var book = new Book();
        if (StringUtils.isNotBlank(parentId)) {
            infoRepo.findById(parentId).ifPresent(book::setInfo);
        }

        return new ModelAndView("/library/book/book-edit", Map.of(
                "entity", book,
                "edit", false
        ));
    }

    @PostMapping(path = "{id}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_UPDATE')")
    public ModelAndView update(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") Book book,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(book.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getTicket().size() > 0 && !StringUtils.equals(book.getInfo().getId(), entity.getInfo().getId())) {
            bindingResult.rejectValue("info", "", "Cannot change info of currently borrowing book (origin: " + entity.getInfo().getTitle() + ")");
        }

        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
            book.setImage(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/book/book-edit", Map.of(
                    "entity", book,
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(book);
        redirectAttributes.addFlashAttribute("highlight", book.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("Item updated successfully").success();
        return new ModelAndView("redirect:/library/books/books");
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_CREATE')")
    public ModelAndView create(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") Book entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
            entity.setImage(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/book/book-edit", Map.of(
                    "entity", entity,
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        ui.toast("Item created successfully").success();
        return new ModelAndView("redirect:/library/books/books");
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_DELETE')")
    public ModelAndView confirmDelete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/book-delete", Map.of(
                "entity", entity,
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_DELETE')")
    public ModelAndView delete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getTicket().size() > 0) {
            ui.toast("Book was borrowed. Cannot delete").error();
            return new ModelAndView("redirect:/library/books/books");
        }

        entity.getBorrows().forEach(b -> {
            final var detail = new SimpleBookDetail();
            BeanUtils.copyProperties(entity, detail);
            b.setBookDetail(detail);
            borrowRepo.save(b);
        });
        repo.delete(entity);
        ui.toast("Book delete succeed").success();
        return new ModelAndView("redirect:/library/books/books");
    }
}
