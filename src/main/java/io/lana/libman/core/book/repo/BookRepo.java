package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends PagingAndSortingRepository<Book, String> {
}
