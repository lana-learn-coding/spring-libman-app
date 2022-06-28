package io.lana.libman.core.book.support;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SimpleBookDetail implements BookDetails {
    private String id;

    private String title;

    private String authorName;

    private String publisherName;

    private String seriesName;

    private Integer releaseYear;

    private Set<String> genresSet = new HashSet<>();

    private double borrowCost;

    @Override
    public String getGenresName() {
        return String.join(",", genresSet);
    }
}
