package io.lana.libman.core.book.controller;


import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.controller.dto.CreateBookInfoDto;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.book.support.SimpleBookDetail;
import io.lana.libman.core.services.file.ImageService;
import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.stream.Collectors;

@Validated
@Controller
@RequestMapping("/library/books/infos")
@RequiredArgsConstructor
class BookInfoController {
    private final BookInfoRepo repo;

    private final BookRepo bookRepo;

    private final BookBorrowRepo borrowRepo;

    private final ImageService imageService;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query,
                               @PageableDefault(6) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var page = StringUtils.isBlank(query)
                ? bookRepo.findAllByInfoId(id, pageable)
                : bookRepo.findAllByInfoIdAndQuery(id, "%" + query + "%", pageable);
        return new ModelAndView("/library/book/info-detail", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @GetMapping("{id}/history")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKBORROW_READ')")
    public ModelAndView history(@PathVariable String id, @RequestParam(required = false) String query,
                                @PageableDefault(5) @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = borrowRepo.findAllByBookInfoIdAndQuery(id, query, pageable);
        return new ModelAndView("/library/book/info-history", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String genreId,
                              @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        genreId = StringUtils.trimToNull(genreId);
        final var page = StringUtils.isAllBlank(query, genreId)
                ? repo.findAll(pageable)
                : repo.findAllByQuery(query, genreId, pageable);
        return new ModelAndView("/library/book/info-index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_CREATE')")
    public ModelAndView create() {
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", new CreateBookInfoDto(),
                "edit", false
        ));
    }

    @PostMapping(path = "{id}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_UPDATE')")
    public ModelAndView update(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") BookInfo entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 500, 600);
            entity.setImage(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/book/info-edit", Map.of(
                    "entity", entity,
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("Item updated successfully").success();
        return new ModelAndView("redirect:/library/books/infos");
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_CREATE')")
    public ModelAndView create(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") CreateBookInfoDto form,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 500, 600);
            form.setImage(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/book/info-edit", Map.of(
                    "entity", form,
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        final var entity = new BookInfo();
        BeanUtils.copyProperties(form, entity);
        repo.save(entity);
        for (int i = 0; i < form.getNumberOfBooks(); i++) {
            final var related = new Book();
            related.setInfo(entity);
            related.setShelf(Shelf.storage());
            bookRepo.save(related);
        }

        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        ui.toast("Item created successfully").success();
        return new ModelAndView("redirect:/library/books/infos");
    }

    @GetMapping("autocomplete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> autocomplete(@RequestParam(required = false, name = "q") String query) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(Pageable.ofSize(6))
                : repo.findAllByTitleLikeIgnoreCase("%" + query + "%", Pageable.ofSize(6));
        final var data = page.stream()
                .map(t -> Map.of("id", t.getId(), "text", t.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "results", data
        ));
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_DELETE')")
    public ModelAndView confirmDelete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/info-delete", Map.of(
                "entity", entity,
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_DELETE')")
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getBooksCount() > 0) {
            ui.toast("Info related to " + entity.getBooksCount() + " books. Cannot delete").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/library/books/infos");
        }

        repo.delete(entity);
        ui.toast("Book info delete succeed").success();
        return new ModelAndView("redirect:/library/books/infos");
    }

    @PostMapping("{id}/force-delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOKINFO_DELETE') && hasAnyAuthority('ADMIN','FORCE')")
    public ModelAndView forceDelete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getBooks().stream().anyMatch(b -> !b.getTicket().isEmpty())) {
            ui.toast("Related book is borrowed").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/library/books/infos");
        }

        entity.getBooks()
                .stream()
                .flatMap(book -> book.getBorrows().stream())
                .forEach(b -> {
                    final var detail = new SimpleBookDetail();
                    BeanUtils.copyProperties(entity, detail);
                    b.setBook(null);
                    b.setBookDetail(detail);
                    borrowRepo.save(b);
                });

        bookRepo.deleteAll(entity.getBooks());
        repo.delete(entity);
        ui.toast("Book info and related book delete succeed").success();
        return new ModelAndView("redirect:/library/books/infos");
    }

}
