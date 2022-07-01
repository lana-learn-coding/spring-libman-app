package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
public interface IncomeRepo extends PagingAndSortingRepository<Income, String> {
    @Query(value = "select i from Income i left join fetch i.borrows b left join fetch b.reader r left join fetch r.account a " +
            "where (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query)) " +
            "and (cast(:fromDate as java.time.LocalDate) is null or :fromDate <= i.returnDate) and (cast(:to as java.time.LocalDate) is null or :to >= i.returnDate)",
            countQuery = "select count(distinct i.id) from Income i left join i.borrows b left join b.reader r left join r.account a " +
                    "where (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query)) " +
                    "and (cast(:fromDate as java.time.LocalDate) is null or :fromDate <= i.returnDate) and (cast(:to as java.time.LocalDate) is null or :to >= i.returnDate)")
    Page<Income> findAllInRangeByQuery(String query, LocalDate fromDate, LocalDate to, Pageable pageable);

    @Query(value = "select count(distinct r.id) as readerCount, sum(i.totalCost) as totalCost, sum(i.borrowsCount) as borrowsCount from Income i left join i.borrows b left join b.reader r left join r.account a " +
            "where (:query is null or lower(a.email) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query) or lower(a.phone) like lower(:query)) " +
            "and (cast(:fromDate as java.time.LocalDate) is null or :fromDate <= i.returnDate) and (cast(:to as java.time.LocalDate) is null or :to >= i.returnDate)")
    Map<String, Object> findSummaryInRangeByQuery(String query, LocalDate fromDate, LocalDate to);
}

