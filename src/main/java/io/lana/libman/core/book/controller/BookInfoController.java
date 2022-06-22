package io.lana.libman.core.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.controller.dto.CreateBookInfoDto;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.file.ImageService;
import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Validated
@Controller
@RequestMapping("/library/books/infos")
@RequiredArgsConstructor
class BookInfoController {
    private final BookInfoRepo repo;

    private final BookRepo bookRepo;

    private final ImageService imageService;

    private final ObjectMapper objectMapper;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query, Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var page = StringUtils.isBlank(query)
                ? bookRepo.findAllByInfoId(id, pageable)
                : bookRepo.findAllByInfoIdAndQuery(id, query, pageable);
        return new ModelAndView("/library/book/info-detail", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String genreId, Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = StringUtils.isAllBlank(query, genreId)
                ? repo.findAll(pageable)
                : repo.findAllByQuery(query, genreId, pageable);
        return new ModelAndView("/library/book/info-index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_INFO_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_INFO_CREATE')")
    public ModelAndView create() {
        return new ModelAndView("/library/book/info-edit", Map.of(
                "entity", new CreateBookInfoDto(),
                "edit", false
        ));
    }

    @PostMapping(path = "{id}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_INFO_UPDATE')")
    public ModelAndView update(@PathVariable String id,
                               @RequestPart(required = false) MultipartFile file,
                               @Valid @ModelAttribute("entity") BookInfo entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (repo.existsByTitleIgnoreCaseAndIdNot(entity.getTitle(), entity.getId())) {
            bindingResult.rejectValue("title", "title.exists", "The title was already taken");
        }
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
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

        entity.setId(id);
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("Item updated successfully").success();
        return new ModelAndView("redirect:/library/books/infos");
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','BOOK_INFO_CREATE')")
    public ModelAndView create(@RequestPart(required = false) MultipartFile file,
                               @Valid @ModelAttribute("entity") CreateBookInfoDto form,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (repo.existsByTitleIgnoreCase(form.getTitle())) {
            bindingResult.rejectValue("title", "title.exists", "The title was already taken");
        }
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
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

        final var entity = objectMapper.convertValue(form, BookInfo.class);
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
}
