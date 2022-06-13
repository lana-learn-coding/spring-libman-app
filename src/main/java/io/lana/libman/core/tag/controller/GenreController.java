package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.repo.AuthorRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/genres")
public class GenreController extends TaggedCrudController<Author> {
    protected GenreController(AuthorRepo authorRepo) {
        super(authorRepo);
    }

    @Override
    protected String getViewDir() {
        return "/tag/genre";
    }
}
