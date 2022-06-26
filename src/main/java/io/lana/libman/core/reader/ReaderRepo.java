package io.lana.libman.core.reader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReaderRepo extends PagingAndSortingRepository<Reader, String> {
    @Override
    List<Reader> findAll();

    @Query(value = "select r from Reader r left join fetch r.account a " +
            "where (:query is null or lower(a.phone) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query)) " +
            "and (:email is null or lower(a.email) like lower(:email))",
            countQuery = "select count(r.id) from Reader r left join r.account " +
                    "where (:query is null or lower(r.account.phone) like lower(:query) or lower(r.account.firstName) like lower(:query) or lower(r.account.lastName) like lower(:query)) " +
                    "and (:email is null or lower(r.account.email) like lower(:email))")
    Page<Reader> findAllByQueryAndEmail(String query, String email, Pageable pageable);

    @Query(value = "select r from Reader r left join fetch r.account a " +
            "where (:query is null or lower(a.phone) like lower(:query) or lower(a.firstName) like lower(:query) or lower(a.lastName) like lower(:query)) " +
            "or (lower(a.email) like lower(:query))",
            countQuery = "select count(r.id) from Reader r left join r.account a " +
                    "where (:query is null or lower(r.account.phone) like lower(:query) or lower(r.account.firstName) like lower(:query) or lower(r.account.lastName) like lower(:query)) " +
                    "or (lower(a.email) like lower(:query))")
    Page<Reader> findAllByQuery(String query, Pageable pageable);
}
