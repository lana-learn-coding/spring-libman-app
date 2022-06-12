package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.Publisher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepo extends PagingAndSortingRepository<Publisher, String> {
}
