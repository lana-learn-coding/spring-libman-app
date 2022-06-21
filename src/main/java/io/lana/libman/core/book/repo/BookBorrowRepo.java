package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookBorrow;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookBorrowRepo extends PagingAndSortingRepository<BookBorrow, String> {
}
