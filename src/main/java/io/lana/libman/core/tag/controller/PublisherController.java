package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Publisher;
import io.lana.libman.core.tag.repo.PublisherRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/publishers")
public class PublisherController extends TaggedCrudController<Publisher> {
    protected PublisherController(PublisherRepo repo) {
        super(repo);
    }

    @Override
    protected String getName() {
        return "Publishers";
    }

    @Override
    protected String getAuthority() {
        return "PUBLISHER";
    }
}
