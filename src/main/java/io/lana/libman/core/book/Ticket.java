package io.lana.libman.core.book;

import io.lana.libman.core.reader.Reader;
import io.lana.libman.support.data.NamedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Ticket extends NamedEntity {

    public Ticket() {
        name = id;
    }

    @OneToMany(mappedBy = "ticket")
    private Set<BookBorrow> borrows = new LinkedHashSet<>();

    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.returned is false and b.due_date < NOW() and b.ticket_id = id)")
    private int overDuesCount;

    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.returned is false and b.ticket_id = id)")
    private int borrowingsCount;

    @Formula("(SELECT COUNT(b.id) FROM book_borrow b WHERE b.ticket_id = id)")
    private int borrowsCount;

    public Reader getReader() {
        return getBorrows().stream().filter(b -> b.getReader() != null)
                .findFirst().map(BookBorrow::getReader).orElse(null);
    }
}
