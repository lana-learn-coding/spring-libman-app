package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetailConverter;
import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.validate.DateAfterNow;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @JoinColumn(foreignKey = @ForeignKey(name = "reader_id", foreignKeyDefinition = "FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE SET NULL"))
    private Reader reader;

    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "book_id", foreignKeyDefinition = "FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL"))
    private Book book;

    private String ticketId = id;

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

    private Double totalCost;

    @DateAfterNow
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
        final var overDueDays = ChronoUnit.DAYS.between(dueDate.plusDays(1), LocalDate.now());
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
        return ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()) < 48;
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
