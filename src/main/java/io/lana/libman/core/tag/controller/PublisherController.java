package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.tag.Publisher;
import io.lana.libman.core.tag.repo.PublisherRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library/tags/publishers")
class PublisherController extends TaggedCrudController<Publisher, BookInfo> {
    private final BookInfoRepo bookInfoRepo;

    protected PublisherController(PublisherRepo repo, BookInfoRepo bookInfoRepo) {
        super(repo);
        this.bookInfoRepo = bookInfoRepo;
    }

    @Override
    protected String getName() {
        return "Publishers";
    }

    @Override
    protected String getAuthority() {
        return "PUBLISHER";
    }

    @Override
    protected Page<BookInfo> getRelationsPage(String id, String query, Pageable pageable) {
        query = StringUtils.defaultIfBlank(query, "");
        return bookInfoRepo.findAllByPublisherIdAndTitleLike(id, "%" + query + "%", pageable);
    }
}
