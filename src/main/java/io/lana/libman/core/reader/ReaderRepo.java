package io.lana.libman.core.reader;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReaderRepo extends PagingAndSortingRepository<Reader, String> {
    @Override
    List<Reader> findAll();
}
