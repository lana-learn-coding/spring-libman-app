package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetailConverter;
import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "ticket_id", foreignKeyDefinition = "FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE"))
    private Ticket ticket;

    @Column(columnDefinition = "TEXT")
    private String note;

    @NotNull
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate = LocalDate.now();

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
}
