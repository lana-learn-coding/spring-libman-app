package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInfoRepo extends PagingAndSortingRepository<BookInfo, String> {
    Page<BookInfo> findAllByAuthorIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllByGenresIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllByGenresId(String id, Pageable pageable);

    Page<BookInfo> findAllByPublisherIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    Page<BookInfo> findAllBySeriesIdAndTitleLikeIgnoreCase(String id, String name, Pageable pageable);

    boolean existsByTitleIgnoreCaseAndIdNot(String name, String id);

    boolean existsByTitleIgnoreCase(String name);

    @Query("select b from BookInfo b " +
            "join b.author a join b.publisher p join b.series s " +
            "where b.title like :query or a.name like :query or p.name like :query or s.name like :query")
    Page<BookInfo> findAllByQuery(String query, Pageable pageable);

}
