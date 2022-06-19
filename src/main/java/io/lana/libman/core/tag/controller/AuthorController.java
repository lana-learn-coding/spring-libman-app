package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.repo.AuthorRepo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Objects;


@Controller
@RequestMapping("/library/tags/authors")
class AuthorController extends TaggedCrudController<Author, BookInfo> {
    private final BookInfoRepo bookInfoRepo;

    protected AuthorController(AuthorRepo authorRepo, BookInfoRepo bookInfoRepo) {
        super(authorRepo);
        this.bookInfoRepo = bookInfoRepo;
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

    @Override
    protected void validateEntity(Author entity, BindingResult bindingResult, String id) {
        if (Objects.nonNull(entity.getDateOfDeath()) && entity.getDateOfDeath().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dateOfDeath", "", "Date of death must not after now");
        }

        if (Objects.nonNull(entity.getDateOfBirth()) && entity.getDateOfBirth().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dateOfBirth", "", "Date of death must not after now");
        }

        if (ObjectUtils.allNotNull(entity.getDateOfBirth(), entity.getDateOfDeath()) && entity.getDateOfBirth().isAfter(entity.getDateOfDeath())) {
            bindingResult.rejectValue("dateOfBirth", "", "Date of birth must before date of death");
            bindingResult.rejectValue("dateOfDeath", "", "Date of death must after date of birth");
        }
    }
}
