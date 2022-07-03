package io.lana.libman.core.services.mail;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

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

    @Setter(AccessLevel.NONE)
    private List<String> lines = new ArrayList<>();

    private String sub;

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

    public MailTemplate lines(String line, String... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    public MailTemplate setLines(Collection<String> lines) {
        this.lines = new ArrayList<>(lines);
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
                .lines("you forgot your password for Libman. If this is true, click below to reset your password")
                .action("Change Password")
                .sub("If you have remember your password you can safely ignore his email.");
    }

    public static MailTemplate ofTemplate(String template) {
        return new MailTemplate(template);
    }
}
