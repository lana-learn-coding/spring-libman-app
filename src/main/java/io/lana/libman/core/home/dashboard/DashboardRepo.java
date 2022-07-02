package io.lana.libman.core.home.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
class DashboardRepo {
    @PersistenceContext
    private EntityManager em;

    public DashboardSummary countDashboardSummary() {
        final var result = em.createQuery("select (select count(r) from Reader r) as readersCount, " +
                        "(select count(b) from Book b) as booksCount, " +
                        "(select count(br) from BookBorrow br where br.returned is false) as borrowsCount, " +
                        "(select count(d) from BookBorrow d where d.returned is false and d.dueDate < :now) as overDuesCount " +
                        "from Reader r", Tuple.class)
                .setParameter("now", LocalDate.now())
                .setMaxResults(1)
                .getSingleResult();
        return new DashboardSummary((long) result.get("readersCount"), (long) result.get("booksCount"),
                (long) result.get("borrowsCount"), (long) result.get("overDuesCount"));
    }
}
