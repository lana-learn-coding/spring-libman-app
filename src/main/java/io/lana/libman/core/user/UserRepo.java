package io.lana.libman.core.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository<User, String> {
    @Query("select u from User u left join fetch u.roles r left join fetch r.permissions left join fetch u.reader re left join fetch re.favorites " +
            "where u.username = :username or u.email = :username")
    Optional<User> findUserForAuth(String username);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, String id);

    @Query(value = "select distinct u from User u left join fetch u.roles r left join fetch r.permissions p left join fetch u.reader re " +
            "where (:reader is false or p.name = 'LIBRARIAN') and (:query is null or lower(u.username) like lower(:query) or lower(u.phone) like lower(:query) or lower(u.email) like lower(:query))",
            countQuery = "select count(distinct u.id) from User u left join u.roles r left join r.permissions p " +
                    "where (:reader is false or p.name = 'LIBRARIAN') and (:query is null or lower(u.username) like lower(:query) or lower(u.phone) like lower(:query) or lower(u.email) like lower(:query))")
    Page<User> findAllByQuery(String query, boolean reader, Pageable pageable);

    @Override
    List<User> findAll();
}
