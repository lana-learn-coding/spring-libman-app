package io.lana.libman.core.reader;

import io.lana.libman.core.book.BookBorrow;
import io.lana.libman.core.book.BookInfo;
import io.lana.libman.core.user.User;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Reader extends AuditableEntity {
    @Valid
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @PrimaryKeyJoinColumn
    private User account;

    @Column(name = "borrow_limit")
    private int borrowLimit = 5;

    @OneToMany(mappedBy = "reader")
    @Where(clause = "returned = false")
    private Set<BookBorrow> borrowingBooks = new LinkedHashSet<>();

    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.returned is false and b.reader_id = id)")
    private int borrowingBooksCount;

    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.returned is false and b.due_date < CURRENT_DATE() and b.reader_id = id)")
    private int overDueBooksCount;

    @OneToMany(mappedBy = "reader")
    private Set<BookBorrow> borrowedBooks = new LinkedHashSet<>();

    public void setAccount(User account) {
        this.account = account;
        this.id = account.getId();
    }

    @ManyToMany
    @JoinTable(
            name = "reader_favorites",
            joinColumns = {@JoinColumn(name = "reader_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE CASCADE"))},
            inverseJoinColumns = {@JoinColumn(name = "book_info_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (book_info_id) REFERENCES book_info(id) ON DELETE CASCADE"))}
    )
    private Set<BookInfo> favorites = new HashSet<>();
}
