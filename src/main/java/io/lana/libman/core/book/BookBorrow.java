package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetailConverter;
import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
public class BookBorrow extends AuditableEntity implements BookDetails {

    @Convert(converter = BookDetailConverter.class)
    @Column(name = "book_detail", columnDefinition = "TEXT")
    private BookDetails bookDetail;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reader_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE SET NULL"))
    private Reader reader;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL"))
    private Book book;

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "income_id", updatable = false, insertable = false)
    private String incomeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (income_id) REFERENCES income(id) ON DELETE CASCADE"))
    private Income income;

    @Column(columnDefinition = "TEXT")
    private String note;

    @NotNull
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate = LocalDate.now();

    @Min(0)
    @Column(name = "borrow_cost", nullable = false)
    private double borrowCost;

    @Min(0)
    @Column(name = "overdue_additional_cost", nullable = false)
    private double overDueAdditionalCost;

    @Column(name = "total_cost")
    private Double totalCost;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate = LocalDate.now();

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "returned")
    private boolean returned;

    @Delegate(types = BookDetails.class)
    public BookDetails getBookDetail() {
        if (book != null) return book;
        return bookDetail;
    }

    @Transient
    public String getIncomeId() {
        if (income == null) return id;
        if (StringUtils.isBlank(incomeId)) return income.getId();
        return incomeId;
    }

    @Transient
    public boolean hasTicket() {
        return income != null;
    }

    @Transient
    public boolean isOverDue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public double getTotalCost() {
        if (totalCost != null) return totalCost;
        totalCost = Precision.round(getTotalBorrowCost() + getTotalOverDueAdditionalCost(), 2);
        return totalCost;
    }

    @Transient
    public long getBorrowedDays() {
        final var borrowedDays = ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
        return borrowedDays > 1 ? borrowedDays : 1;
    }

    @Transient
    public long getOverDueDays() {
        final var overDueDays = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        return overDueDays > 0 ? overDueDays : 0;
    }

    @Transient
    public double getTotalBorrowCost() {
        return Precision.round(borrowCost * getBorrowedDays(), 2);
    }

    @Transient
    public double getTotalOverDueAdditionalCost() {
        return Precision.round(overDueAdditionalCost * getOverDueDays(), 2);
    }

    @Transient
    public boolean canRefund() {
        return ChronoUnit.HOURS.between(borrowDate.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()) < 24;
    }

    @Transient
    public double recalculate() {
        totalCost = null;
        return getTotalCost();
    }

    @PreUpdate
    @PrePersist
    private void calculateTotalCost() {
        if (!returned) totalCost = null;
        if (totalCost != null) return;
        totalCost = Precision.round(getTotalBorrowCost() + getTotalOverDueAdditionalCost(), 2);
    }
}
