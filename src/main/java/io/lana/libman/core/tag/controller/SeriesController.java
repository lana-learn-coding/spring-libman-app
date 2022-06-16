package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.tag.Series;
import io.lana.libman.core.tag.repo.SeriesRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/series")
public class SeriesController extends TaggedCrudController<Series, BookInfo> {
    private final BookInfoRepo bookInfoRepo;

    protected SeriesController(SeriesRepo repo, BookInfoRepo bookInfoRepo) {
        super(repo);
        this.bookInfoRepo = bookInfoRepo;
    }

    @Override
    protected String getName() {
        return "Series";
    }

    @Override
    protected String getAuthority() {
        return "SERIES";
    }

    @Override
    protected Page<BookInfo> getRelationsPage(String id, String query, Pageable pageable) {
        query = StringUtils.defaultIfBlank(query, "");
        return bookInfoRepo.findAllBySeriesIdAndTitleLike(id, "%" + query + "%", pageable);
    }
}
