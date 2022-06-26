package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends PagingAndSortingRepository<Book, String> {
    Page<Book> findAllByShelfIdAndInfoTitleLikeIgnoreCase(String shelfId, String infoName, Pageable pageable);

    Page<Book> findAllByShelfId(String shelfId, Pageable pageable);


    @Query(value = "select b from Book b left join fetch b.info bi left join bi.series s left join fetch b.ticket t left join fetch t.reader r left join fetch r.account a " +
            "where bi.id = :id and (lower(a.email) like lower(:query) or lower(bi.title) like lower(:query) or lower(s.name) like lower(:query))",
            countQuery = "select count(b.id) from Book b left join b.info bi left join bi.series s left join b.ticket t left join t.reader r left join r.account a " +
                    "where bi.id = :id and (lower(a.email) like lower(:query) or lower(bi.title) like lower(:query) or lower(s.name) like lower(:query))")
    Page<Book> findAllByInfoIdAndQuery(String id, String query, Pageable pageable);

    @Query(value = "select b from Book b left join fetch b.info i left join i.series s left join fetch b.ticket t left join fetch b.shelf sh " +
            "where (:query is null or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) and (:shelfId is null or sh.id = :shelfId)",
            countQuery = "select count(b.id) from Book b left join b.info i left join i.series s left join b.shelf sh " +
                    "where (:query is null or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) and (:shelfId is null or sh.id = :shelfId)")
    Page<Book> findAllByQueryAndShelfId(String query, String shelfId, Pageable pageable);

    Page<Book> findAllByInfoId(String id, Pageable pageable);

    @Override
    List<Book> findAll();
}
