package io.lana.libman.core.services.mail;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Qualifier("testMailService")
class TestMailService implements MailService {

    @Override
    public void send(MailTemplate simpleMessage) {
        log.info("== START SENDING MOCK MAIL ==");
        log.info("FROM: {}", simpleMessage.from());
        log.info("TO: {}", StringUtils.join(simpleMessage.to(), ", "));
        if (simpleMessage.cc() != null && simpleMessage.cc().length > 0) {
            log.info("CC: {}", StringUtils.join(simpleMessage.to(), ", "));
        }
        if (simpleMessage.bcc() != null && simpleMessage.bcc().length > 0) {
            log.info("BCC: {}", StringUtils.join(simpleMessage.bcc(), ", "));
        }
        log.info("SUBJECT: {}", simpleMessage.subject());
        log.info("CONTENT:");
        if (StringUtils.isNotBlank(simpleMessage.header())) {
            log.info("  Header: {}", simpleMessage.header());
        }
        simpleMessage.lines().forEach(line -> log.info("  Line: {}", line));
        if (StringUtils.isNotBlank(simpleMessage.action())) {
            log.info("  Action: {} <{}>,", simpleMessage.action(), StringUtils.defaultIfBlank(simpleMessage.link(), "No Link"));
        }
        if (StringUtils.isNotBlank(simpleMessage.sub())) {
            log.info("  Sub: {}", simpleMessage.sub());
        }
        log.info("TEMPLATE: {}", simpleMessage.template());
        log.info("== END SENDING MOCK MAIL ==");
    }

    @Override
    public CompletableFuture<Void> sendAsync(MailTemplate template) {
        send(template);
        return CompletableFuture.completedFuture(null);
    }
}
