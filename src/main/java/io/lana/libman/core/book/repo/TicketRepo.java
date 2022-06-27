package io.lana.libman.core.book.repo;

import io.lana.libman.core.book.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketRepo extends PagingAndSortingRepository<Ticket, String> {
}
