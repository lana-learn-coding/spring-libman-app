package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.repo.GenreRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/genres")
public class GenreController extends TaggedCrudController<Genre> {
    protected GenreController(GenreRepo repo) {
        super(repo);
    }

    @Override
    protected String getName() {
        return "Genres";
    }

    @Override
    protected String getAuthority() {
        return "GENRE";
    }
}
