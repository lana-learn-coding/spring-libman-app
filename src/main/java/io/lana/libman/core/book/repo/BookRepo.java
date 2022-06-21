package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends PagingAndSortingRepository<Book, String> {
    Page<Book> findAllByShelfIdAndInfoTitleLike(String shelfId, String infoName, Pageable pageable);

    Page<Book> findAllByShelfId(String shelfId, Pageable pageable);

    @Override
    List<Book> findAll();
}
