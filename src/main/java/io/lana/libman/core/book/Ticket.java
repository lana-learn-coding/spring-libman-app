package io.lana.libman.core.book;

import io.lana.libman.support.data.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Ticket extends NamedEntity {

    public Ticket() {
        name = id;
    }

    @OneToMany(mappedBy = "ticket")
    private Set<BookBorrow> borrows = new HashSet<>();

    private int borrowingsCount;

    private int borrowsCount;
}
