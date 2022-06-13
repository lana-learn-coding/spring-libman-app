package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Shelf;
import io.lana.libman.core.tag.repo.ShelfRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/shelf")
public class ShelfController extends TaggedCrudController<Shelf> {
    protected ShelfController(ShelfRepo repo) {
        super(repo);
    }

    @Override
    protected String getName() {
        return "Shelf";
    }

    @Override
    protected String getAuthority() {
        return "SHELF";
    }
}
