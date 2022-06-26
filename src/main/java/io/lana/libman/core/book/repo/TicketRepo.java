package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketRepo extends PagingAndSortingRepository<Ticket, String> {

    @Query(value = "select distinct(t) from Ticket t left join fetch t.borrows b left join fetch b.reader r left join fetch r.account a " +
            "where b.returned = false and (:query is null or lower(t.name) like lower(:query) or lower(b.id) like lower(:query)) " +
            "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader))",
            countQuery = "select count(distinct t) from Ticket t left join t.borrows b left join b.reader r left join  r.account a " +
                    "where b.returned = false and (:query is null or lower(t.name) like lower(:query) or lower(b.id) like lower(:query)) " +
                    "and (:reader is null or lower(a.email) like lower(:reader) or lower(a.firstName) like lower(:reader) or lower(a.lastName) like lower(:reader) or lower(a.phone) like lower(:reader))")
    Page<Ticket> findAllBorrowingByQueryAndReader(String query, String reader, Pageable pageable);
}
