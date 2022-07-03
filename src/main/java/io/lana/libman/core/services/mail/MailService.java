package io.lana.libman.core.services.mail;

public interface MailService {
    void send(MailTemplate simpleMessage);


    default void send(MailTemplate... simpleMessages) {
        for (final var simpleMessage : simpleMessages) {
            send(simpleMessage);
        }
    }
}
