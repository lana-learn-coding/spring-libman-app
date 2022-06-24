package io.lana.libman.support.data;

import java.time.Instant;

public interface Auditable {
    Instant getCreatedAt();

    Instant getUpdatedAt();
}
