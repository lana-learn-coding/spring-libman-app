package io.lana.libman.core.book.support;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class SimpleBookDetail implements BookDetails {
    private String id;

    private String title;

    private String authorName;

    private String publisherName;

    private String seriesName;

    private Set<String> genresSet = new HashSet<>();

    @Override
    public String getGenresName() {
        return String.join(",", genresSet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BookDetails other = (BookDetails) obj;
        return Objects.equals(getId(), other.getId());
    }
}
