package io.lana.libman.support.data;

import com.github.f4b6a3.ulid.UlidCreator;

public final class IdUtils {
    private IdUtils() {
    }

    public static String newTimeSortableId() {
        return UlidCreator.getMonotonicUlid().toString();
    }
}
