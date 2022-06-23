package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.file.ImageService;
import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.repo.AuthorRepo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Validated
@Controller
@RequestMapping("/library/tags/authors")
class AuthorController extends TaggedCrudController<Author, BookInfo> {
    private final BookInfoRepo bookInfoRepo;

    private final ImageService imageService;

    protected AuthorController(AuthorRepo authorRepo, BookInfoRepo bookInfoRepo, ImageService imageService) {
        super(authorRepo);
        this.bookInfoRepo = bookInfoRepo;
        this.imageService = imageService;
    }

    @Override
    protected String getName() {
        return "Authors";
    }

    @Override
    protected String getAuthority() {
        return "AUTHOR";
    }

    @Override
    protected String getEditFormView() {
        return "/library/tag/author/edit";
    }

    @Override
    protected Page<BookInfo> getRelationsPage(String id, String query, Pageable pageable) {
        query = StringUtils.defaultIfBlank(query, "");
        return bookInfoRepo.findAllByAuthorIdAndTitleLikeIgnoreCase(id, "%" + query + "%", pageable);
    }

    @Override
    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query, Pageable pageable) {
        final var modelAndView = super.detail(id, query, pageable);
        modelAndView.setViewName("/library/tag/author/detail");
        return modelAndView;
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView create(@Validated @ModelAttribute("entity") Author entity,
                               @RequestPart(required = false) MultipartFile file,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        validateEntity(entity, bindingResult, null);
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
            entity.setImage(imageService.save(image).getUri());
        }
        return store(entity, bindingResult, redirectAttributes, null);
    }

    @PostMapping(path = "{id}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView update(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") Author entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");
        validateEntity(entity, bindingResult, entity.getId());
        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "image")) {
            final var image = imageService.crop(file, 200, 200);
            entity.setImage(imageService.save(image).getUri());
        }
        entity.setId(entity.getId());
        return store(entity, bindingResult, redirectAttributes, entity.getId());
    }

    private void validateEntity(Author entity, BindingResult bindingResult, String id) {
        if (ObjectUtils.allNotNull(entity.getDateOfBirth(), entity.getDateOfDeath()) && entity.getDateOfBirth().isAfter(entity.getDateOfDeath())) {
            bindingResult.rejectValue("dateOfBirth", "", "Date of birth must before date of death");
            bindingResult.rejectValue("dateOfDeath", "", "Date of death must after date of birth");
        }
    }
}
