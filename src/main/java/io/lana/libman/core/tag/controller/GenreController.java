package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.repo.GenreRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/genres")
public class GenreController extends TaggedCrudController<Genre, BookInfo> {
    private final BookInfoRepo bookInfoRepo;

    protected GenreController(GenreRepo repo, BookInfoRepo bookInfoRepo) {
        super(repo);
        this.bookInfoRepo = bookInfoRepo;
    }

    @Override
    protected String getName() {
        return "Genres";
    }

    @Override
    protected String getAuthority() {
        return "GENRE";
    }

    @Override
    protected Page<BookInfo> getRelationsPage(String id, String query, Pageable pageable) {
        query = StringUtils.defaultIfBlank(query, "");
        return bookInfoRepo.findAllByGenresIdAndTitleLike(id, "%" + query + "%", pageable);
    }
}
