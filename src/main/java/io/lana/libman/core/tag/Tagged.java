package io.lana.libman.core.tag;

import io.lana.libman.support.data.Named;

public interface Tagged extends Named {
    String getAbout();

    void setAbout(String about);

    int getBooksCount();
}
