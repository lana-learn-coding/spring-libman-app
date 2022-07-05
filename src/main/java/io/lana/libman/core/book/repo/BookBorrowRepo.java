package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookBorrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BookBorrowRepo extends PagingAndSortingRepository<BookBorrow, String> {

    Set<BookBorrow> findAllByIdInAndReturnedIsFalse(Collection<String> ids);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i join b.income c " +
            "where c.id = :id and (:query is null or lower(i.title) like lower(:query))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i join b.income c " +
                    "where c.id = :id and (:query is null or lower(i.title) like lower(:query))")
    Page<BookBorrow> findAllByIncomeIdAndQuery(String id, String query, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info where b.reader.id = :id and b.returned is false",
            countQuery = "select count(b.id) from BookBorrow b where b.reader.id = :id and b.returned is false")
    Page<BookBorrow> findAllBorrowingByReaderId(String id, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r " +
            "where r.id = :id and b.returned is false and lower(i.title) like lower(:query)",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r " +
                    "where r.id = :id and b.returned is false and lower(i.title) like lower(:query) ")
    Page<BookBorrow> findAllBorrowingByReaderIdAndQuery(String id, String query, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r " +
            "where r.id = :id and b.returned is false and (:query is null or lower(i.title) like lower(:query)) and (:#{#ids == null} = true or b.id not in (:ids))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r " +
                    "where r.id = :id and b.returned is false and (:query is null or lower(i.title) like lower(:query)) and (:#{#ids == null} = true or b.id not in (:ids))")
    List<BookBorrow> findAllBorrowingByReaderIdAndQueryExclude(String id, String query, Collection<String> ids);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r " +
            "where r.id = :id and b.returned is true and (:query is null or lower(i.title) like lower(:query))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r " +
                    "where r.id = :id and b.returned is true and (:query is null or lower(i.title) like lower(:query))")
    Page<BookBorrow> findAllReturnedByReaderIdAndQuery(String id, String query, Pageable pageable);


    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch b.reader r left join fetch r.account a " +
            "where i.id = :id and (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query))",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i left join b.reader r left join r.account a " +
                    "where i.id = :id and (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query))")
    Page<BookBorrow> findAllByBookInfoIdAndQuery(String id, String query, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch i.series s left join fetch b.reader r left join fetch r.account a " +
            "where (:query is null  or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) " +
            "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader))",
            countQuery = "select count(b.id) from BookBorrow b left join  b.book bo left join  bo.info i left join  i.series s left join  b.reader r left join  r.account a " +
                    "where (:query is null  or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) " +
                    "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader))")
    Page<BookBorrow> findAllByQueryAndReader(String query, String reader, Pageable pageable);

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i left join fetch i.series s left join fetch b.reader r left join fetch r.account a " +
            "where (:query is null  or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) " +
            "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader)) " +
            "and b.returned is false",
            countQuery = "select count(b.id) from BookBorrow b left join  b.book bo left join  bo.info i left join  i.series s left join  b.reader r left join  r.account a " +
                    "where (:query is null  or lower(i.title) like lower(:query) or lower(s.name) like lower(:query)) " +
                    "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader)) " +
                    "and b.returned is false")
    Page<BookBorrow> findAllBorrowingByQueryAndReader(String query, String reader, Pageable pageable);
}
