package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.repo.AuthorRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/library/tags/authors")
public class AuthorController extends TaggedCrudController<Author, BookInfo> {
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
    protected Page<BookInfo> getRelationsPage(String id, String query, Pageable pageable) {
        query = StringUtils.defaultIfBlank(query, "");
        return bookInfoRepo.findAllByAuthorIdAndTitleLike(id, "%" + query + "%", pageable);
    }
}
