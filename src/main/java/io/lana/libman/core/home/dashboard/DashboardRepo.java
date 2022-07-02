package io.lana.libman.core.home.dashboard;

import io.lana.libman.core.book.BookBorrow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Double> countBorrowByDayInLast7Days() {
        final var result = em.createQuery("select b.borrowDate as date, count(b.id) as count from BookBorrow b where b.borrowDate >= :last group by b.borrowDate order by b.borrowDate asc", Tuple.class)
                .setParameter("last", LocalDate.now().minusDays(7))
                .getResultList();
        return result.stream().collect(Collectors.toMap(r -> r.get("date").toString(), r -> ((Long) r.get("count")).doubleValue(),
                (a, b) -> a, LinkedHashMap::new));
    }

    public Map<String, Double> countIncomeByDayInLast7Days() {
        final var result = em.createQuery("select b.returnDate as date, sum(b.totalCost) as count from Income b where b.returnDate >= :last group by b.returnDate order by b.returnDate asc", Tuple.class)
                .setParameter("last", LocalDate.now().minusDays(7))
                .getResultList();
        return result.stream().collect(Collectors.toMap(r -> r.get("date").toString(), r -> (Double) r.get("count"),
                (a, b) -> a, LinkedHashMap::new));
    }

    public Map<String, Double> countReaderByDayInLast7Days() {
        final var result = em.createQuery("select b.borrowDate as date, count(distinct b.reader.id) as count from BookBorrow b where b.borrowDate >= :last group by b.borrowDate order by b.borrowDate asc", Tuple.class)
                .setParameter("last", LocalDate.now().minusDays(7))
                .getResultList();
        return result.stream().collect(Collectors.toMap(r -> r.get("date").toString(), r -> ((Long) r.get("count")).doubleValue(),
                (a, b) -> a, LinkedHashMap::new));
    }

    public List<BookBorrow> getTopOverDueBorrow() {
        return em.createQuery("select b from BookBorrow b left join fetch b.reader r left join fetch r.account a left join fetch b.book bo left join fetch bo.info i left join fetch i.series s " +
                        "where b.returned is false and b.dueDate < :now order by b.dueDate desc", BookBorrow.class)
                .setParameter("now", LocalDate.now())
                .setMaxResults(4)
                .getResultList();
    }

    public Map<String, Map<String, Double>> countIncomeByDayInLast30Days() {
        final var result = em.createQuery("select b.returnDate as date, sum(b.totalCost) as totalCost, sum(b.totalBorrowCost) as borrowCost, sum(b.totalOverDueAdditionalCost) as overDueCost from Income b " +
                        "where b.returnDate >= :last and b.returnDate < :now group by b.returnDate order by b.returnDate asc", Tuple.class)
                .setParameter("last", LocalDate.now().minusDays(31)).setParameter("now", LocalDate.now())
                .getResultList();
        return result.stream().collect(Collectors.toMap(r -> r.get("date").toString(),
                r -> Map.of("totalCost", (Double) r.get("totalCost"), "borrowCost", (Double) r.get("borrowCost"), "overDueCost", (Double) r.get("overDueCost")),
                (a, b) -> a, LinkedHashMap::new));
    }
}
