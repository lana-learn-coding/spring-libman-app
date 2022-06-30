package io.lana.libman.core.book;

import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;
import org.hibernate.annotations.Formula;

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
    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.ticket_id = id)")
    private int borrowsCount;

    @Column(name = "total_cost", nullable = false)
    private double totalCost = 0d;

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
    public void addTotalCost(double totalCost) {
        this.totalCost = Precision.round(totalCost + this.totalCost, 2);
    }
}
