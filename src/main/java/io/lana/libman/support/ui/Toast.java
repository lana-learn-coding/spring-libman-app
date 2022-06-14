package io.lana.libman.support.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Getter
public class Toast {

    private final String message;

    private final HttpServletRequest request;

    private String variant = "light";

    private String color = Type.PRIMARY.getColor();

    private String icon;


    protected Toast(String message, HttpServletRequest request) {
        this.message = message;
        this.request = request;
    }

    protected Toast bind() {
        this.request.getSession().setAttribute("ui.toast", this);
        return this;
    }

    public Toast info() {
        return type(Type.PRIMARY).icon(Icon.INFO);
    }

    public Toast success() {
        return type(Type.SUCCESS).icon(Icon.SUCCESS);
    }

    public Toast error() {
        return type(Type.ERROR).icon(Icon.ERROR);
    }

    public Toast warning() {
        return type(Type.WARNING).icon(Icon.WARNING);
    }

    public Toast type(Type type) {
        this.color = type.getColor();
        return bind();
    }

    public Toast solid() {
        this.variant = "dark";
        return bind();
    }

    public Toast outlined() {
        this.variant = "outline";
        return bind();
    }

    public Toast icon(Icon icon) {
        this.icon = icon.getName();
        return bind();
    }

    public Toast icon(String iconName) {
        this.icon = iconName;
        return bind();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        SUCCESS("success"),
        INFO("info"),
        WARNING("warning"),
        ERROR("danger"),
        LIGHT("light"),
        DARK("dark"),
        PRIMARY("primary"),
        SECONDARY("secondary");

        private final String color;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Icon {
        SUCCESS("check-circle"),
        INFO("info"),
        WARNING("alert-triangle"),
        ERROR("alert-circle");

        private final String name;
    }
}
