package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetailConverter;
import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.IdUtils;
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
    @Column(name = "book_detail")
    private BookDetails bookDetail;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "reader_id", foreignKeyDefinition = "FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE SET NULL"))
    private Reader reader;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "book_id", foreignKeyDefinition = "FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL"))
    private Book book;

    private String ticket = IdUtils.newTimeSortableId();

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
        if (book.getInfo() != null) return book;
        return bookDetail;
    }
}