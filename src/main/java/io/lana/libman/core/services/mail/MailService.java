package io.lana.libman.core.services.mail;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    void send(MailTemplate simpleMessage);

    CompletableFuture<Void> sendAsync(MailTemplate template);

    default void send(MailTemplate... simpleMessages) {
        for (final var simpleMessage : simpleMessages) {
            send(simpleMessage);
        }
    }
}
