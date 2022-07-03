package io.lana.libman.core.services.mail;

import io.lana.libman.config.ConfigFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Primary
@Slf4j
class MailServiceImpl implements MailService {
    private final MailService testMailService;

    private final String from;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;


    MailServiceImpl(@Qualifier("testMailService") MailService testMailService, ConfigFacade config, JavaMailSender mailSender,
                    TemplateEngine templateEngine) {
        this.testMailService = testMailService;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = config.getEmailFrom();
        if (StringUtils.equalsIgnoreCase(from, "test")) {
            log.warn("Test mail config detected (config.mail.from: {}), will log email instead.", from);
        }
    }

    @Override
    public void send(MailTemplate simpleMessage) {
        if (StringUtils.isBlank(simpleMessage.from())) {
            simpleMessage.from(from);
        }
        if (StringUtils.equalsIgnoreCase(from, "test")) {
            testMailService.send(simpleMessage);
            return;
        }
        final var message = mailSender.createMimeMessage();
        try {
            final var mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(simpleMessage.from());
            mimeMessageHelper.setTo(simpleMessage.to());
            mimeMessageHelper.setCc(ObjectUtils.defaultIfNull(simpleMessage.cc(), new String[]{}));
            mimeMessageHelper.setBcc(ObjectUtils.defaultIfNull(simpleMessage.bcc(), new String[]{}));
            mimeMessageHelper.setSubject(StringUtils.defaultString(simpleMessage.subject()));
            mimeMessageHelper.setText(createContent(simpleMessage), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createContent(MailTemplate template) {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("mail", template);
        return templateEngine.process(template.template(), new Context(Locale.ENGLISH, variables));
    }
}
