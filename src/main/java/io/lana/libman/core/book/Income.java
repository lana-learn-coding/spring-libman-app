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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Income extends AuditableEntity {
    @Column(name = "borrows_count", nullable = false, updatable = false)
    private int borrowsCount;

    @Column(name = "total_cost", nullable = false, updatable = false)
    private double totalCost = 0d;

    @Column(name = "total_borrow_cost", nullable = false, updatable = false)
    private double totalBorrowCost = 0d;

    @Column(name = "total_overdue_additional_cost", nullable = false, updatable = false)
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
    public void add(BookBorrow borrow) {
        this.totalCost = Precision.round(borrow.getTotalCost() + this.totalCost, 2);
        this.totalBorrowCost = Precision.round(borrow.getTotalBorrowCost() + this.totalCost, 2);
        this.totalOverDueAdditionalCost = Precision.round(borrow.getTotalOverDueAdditionalCost() + this.totalCost, 2);
        this.borrowsCount += 1;
        this.borrows.add(borrow);
    }
}
