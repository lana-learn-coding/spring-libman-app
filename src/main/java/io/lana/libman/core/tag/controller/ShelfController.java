package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.tag.Shelf;
import io.lana.libman.core.tag.repo.ShelfRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/shelf")
class ShelfController extends TaggedCrudController<Shelf, Book> {
    private final BookRepo bookRepo;

    protected ShelfController(ShelfRepo repo, BookRepo bookRepo) {
        super(repo);
        this.bookRepo = bookRepo;
    }

    @Override
    protected String getName() {
        return "Shelf";
    }

    @Override
    protected String getAuthority() {
        return "SHELF";
    }

    @Override
    protected Page<Book> getRelationsPage(String id, String query, Pageable pageable) {
        return StringUtils.isBlank(query)
                ? bookRepo.findAllByShelfId(id, pageable)
                : bookRepo.findAllByShelfIdAndInfoTitleLike(id, "%" + query + "%", pageable);
    }
}
