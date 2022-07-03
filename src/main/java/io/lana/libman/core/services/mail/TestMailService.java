package io.lana.libman.core.services.mail;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("testMailService")
class TestMailService implements MailService {

    @Override
    public void send(MailTemplate simpleMessage) {
        log.info("START SENDING MOCK MAIL: {}", simpleMessage.template());
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
        log.info("  Content: {}", simpleMessage.content());
        if (StringUtils.isNotBlank(simpleMessage.action())) {
            log.info("  Action: {} <{}>,", simpleMessage.action(), StringUtils.defaultIfBlank(simpleMessage.link(), "No Link"));
        }
        if (StringUtils.isNotBlank(simpleMessage.subContent())) {
            log.info("  Content: {}", simpleMessage.subContent());
        }
        log.info("END SENDING MOCK MAIL");
    }
}
