package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInfoRepo extends PagingAndSortingRepository<BookInfo, String> {
    Page<BookInfo> findAllByAuthorIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllByGenresIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllByGenresId(String id, Pageable pageable);

    Page<BookInfo> findAllByPublisherIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllBySeriesIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

}
