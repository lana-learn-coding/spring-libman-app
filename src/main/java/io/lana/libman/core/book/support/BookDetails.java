package io.lana.libman.core.book.support;

import java.util.Set;

public interface BookDetails {
    String getTitle();

    String getAuthorName();

    String getPublisherName();

    String getSeriesName();

    String getGenresName();

    Integer getReleaseYear();

    Set<String> getGenresSet();

    double getBorrowCost();
}
