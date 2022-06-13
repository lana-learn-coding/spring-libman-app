package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Series;
import io.lana.libman.core.tag.repo.SeriesRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/series")
public class SeriesController extends TaggedCrudController<Series> {
    protected SeriesController(SeriesRepo repo) {
        super(repo);
    }

    @Override
    protected String getViewDir() {
        return "/tag/series";
    }
}
