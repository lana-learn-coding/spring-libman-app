package io.lana.libman.support.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;

@Component
@RequestScope
@RequiredArgsConstructor
public class UIFacade {
    private final HttpServletRequest request;

    public Toast toast(String message) {
        return new Toast(message, request).bind();
    }
}
