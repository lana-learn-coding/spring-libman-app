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
    Page<Book> findAllByShelfIdAndInfoTitleLike(String shelfId, String infoName, Pageable pageable);

    Page<Book> findAllByShelfId(String shelfId, Pageable pageable);


    @Query(value = "select b from Book b join fetch b.info bi join bi.series s join fetch b.ticket t join fetch t.reader r join fetch r.account a " +
            "where bi.id = :id and (a.email like :query or bi.title like :query or s.name like :query)",
            countQuery = "select count(b.id) from Book b join b.info bi join bi.series s join b.ticket t join t.reader r join r.account a "
                    + "where bi.id = :id and (a.email like :query or bi.title like :query or s.name like :query)")
    Page<Book> findAllByInfoIdAndQuery(String id, String query, Pageable pageable);

    Page<Book> findAllByInfoId(String id, Pageable pageable);

    @Override
    List<Book> findAll();
}
