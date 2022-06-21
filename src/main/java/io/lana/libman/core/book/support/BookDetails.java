package io.lana.libman.core.book.support;

import java.util.Set;

public interface BookDetails {
    String getId();

    String getTitle();

    String getAuthorName();

    String getPublisherName();

    String getSeriesName();

    String getGenresName();

    Set<String> getGenresSet();
}
