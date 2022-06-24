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

    @Query(value = "select b from BookBorrow b left join fetch b.book bo left join fetch bo.info i " +
            "where b.reader.id = :id and b.returned is false and lower(i.title) like lower(:query)",
            countQuery = "select count(b.id) from BookBorrow b left join b.book bo left join bo.info i " +
                    "where b.reader.id = :id and b.returned is false and lower(i.title) like lower(:query) ")
    Page<BookBorrow> findAllBorrowingByReaderIdAndQuery(String id, String query, Pageable pageable);

}
