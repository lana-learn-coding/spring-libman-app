package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookBorrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookBorrowRepo extends PagingAndSortingRepository<BookBorrow, String> {

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info where b.reader.id = :id and b.returned is false",
            countQuery = "select count(b.id) from BookBorrow b where b.reader.id = :id and b.returned is false")
    Page<BookBorrow> findAllBorrowingByReaderId(String id, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r " +
            "where r.id = :id and b.returned is false and lower(i.title) like lower(:query)",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r " +
                    "where r.id = :id and b.returned is false and lower(i.title) like lower(:query) ")
    Page<BookBorrow> findAllBorrowingByReaderIdAndQuery(String id, String query, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r " +
            "where r.id = :id and (:query is null or lower(i.title) like lower(:query))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r " +
                    "where r.id = :id and (:query is null or lower(i.title) like lower(:query))")
    Page<BookBorrow> findAllByReaderIdAndQuery(String id, String query, Pageable pageable);


    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r left join fetch r.account a " +
            "where i.id = :id and (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r left join r.account a " +
                    "where i.id = :id and (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query))")
    Page<BookBorrow> findAllByBookInfoIdAndQuery(String id, String query, Pageable pageable);
}
