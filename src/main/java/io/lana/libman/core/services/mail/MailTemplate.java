package io.lana.libman.core.services.mail;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class MailTemplate {
    public static final String EMPTY = "";

    private String from;

    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private Date sentDate;

    private String subject;

    @Setter(AccessLevel.NONE)
    private String template;

    private String header;

    private String action;

    private String link;

    private String content;

    private String subContent;

    public MailTemplate to(String to) {
        this.to = new String[]{to};
        return this;
    }

    public MailTemplate cc(String cc) {
        this.cc = new String[]{cc};
        return this;
    }

    public MailTemplate bcc(String bcc) {
        this.bcc = new String[]{bcc};
        return this;
    }

    private MailTemplate(String template) {
        this.template = template;
    }

    public static MailTemplate defaultTemplate() {
        return new MailTemplate("mail");
    }

    public static MailTemplate changePassword() {
        return new MailTemplate("mail")
                .header("Change Password")
                .content("you forgot your password for Libman. If this is true, click below to reset your password")
                .action("Change Password")
                .subContent("If you have remember your password you can safely ignore his email.");
    }

    public static MailTemplate ofTemplate(String template) {
        return new MailTemplate(template);
    }
}
