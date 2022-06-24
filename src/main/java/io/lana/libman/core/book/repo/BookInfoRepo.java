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

    @Query(value = "select distinct b from BookInfo b " +
            "left join fetch b.author a left join fetch b.publisher p left join fetch b.series s left join b.genres g " +
            "where (:query is null or lower(b.title) like lower(:query) or lower(a.name) like lower(:query) or lower(p.name) like lower(:query) or lower(s.name) like lower(:query)) " +
            "and (:genreId is null or g.id like :genreId)",
            countQuery = "select count(distinct b.id) from BookInfo b " +
                    "left join b.author a left join b.publisher p left join b.series s left join b.genres g " +
                    "where (:query is null or lower(b.title) like lower(:query) or lower(a.name) like lower(:query) or lower(p.name) like lower(:query) or lower(s.name) like lower(:query)) " +
                    "and (:genreId is null or g.id like :genreId)")
    Page<BookInfo> findAllByQuery(String query, String genreId, Pageable pageable);

}
