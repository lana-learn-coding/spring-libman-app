package io.lana.libman.core.book;

import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Income extends AuditableEntity {
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate = LocalDate.now().plusYears(1000);

    @Column(name = "dueDate", nullable = false)
    private LocalDate dueDate = LocalDate.now().minusYears(1000);

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate = LocalDate.now();

    @Column(name = "borrows_count", nullable = false)
    private int borrowsCount;

    @Column(name = "total_cost", nullable = false)
    private double totalCost = 0d;

    @Column(name = "total_borrow_cost", nullable = false)
    private double totalBorrowCost = 0d;

    @Column(name = "total_overdue_additional_cost", nullable = false)
    private double totalOverDueAdditionalCost = 0d;

    @Transient
    public boolean isSingle() {
        return borrowsCount <= 1;
    }

    @OneToMany(mappedBy = "income")
    private Set<BookBorrow> borrows = new LinkedHashSet<>();

    @Transient
    public Reader getReader() {
        return borrows.stream().findAny().map(BookBorrow::getReader).orElse(null);
    }

    @Transient
    public Income add(BookBorrow borrow) {
        this.totalCost = Precision.round(borrow.getTotalCost() + this.totalCost, 2);
        this.totalBorrowCost = Precision.round(borrow.getTotalBorrowCost() + this.totalBorrowCost, 2);
        this.totalOverDueAdditionalCost = Precision.round(borrow.getTotalOverDueAdditionalCost() + this.totalOverDueAdditionalCost, 2);
        this.borrowsCount += 1;
        if (borrow.getDueDate().isAfter(dueDate)) dueDate = borrow.getDueDate();
        if (borrow.getBorrowDate().isBefore(borrowDate)) borrowDate = borrow.getBorrowDate();
        return this;
    }
}