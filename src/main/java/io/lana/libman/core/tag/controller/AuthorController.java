package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.repo.AuthorRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/authors")
public class AuthorController extends TaggedCrudController<Author> {
    protected AuthorController(AuthorRepo authorRepo) {
        super(authorRepo);
    }

    @Override
    protected String getViewDir() {
        return "/tag/author";
    }
}
