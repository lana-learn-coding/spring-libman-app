package io.lana.libman.core.reader;

import io.lana.libman.core.book.BookBorrow;
import io.lana.libman.core.user.User;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Reader extends AuditableEntity {
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    private User account;

    @Column(name = "borrow_limit")
    private int borrowLimit = 5;

    @OneToMany(mappedBy = "reader")
    private Set<BookBorrow> borrowedBooks = new LinkedHashSet<>();

    public void setAccount(User account) {
        this.account = account;
        this.id = account.getId();
    }
}
