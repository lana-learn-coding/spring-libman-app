package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.BookInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInfoRepo extends PagingAndSortingRepository<BookInfo, String> {
}
