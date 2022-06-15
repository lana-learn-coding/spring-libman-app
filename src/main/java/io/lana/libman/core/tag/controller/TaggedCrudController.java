package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.TaggedEntity;
import io.lana.libman.core.tag.repo.TaggedRepo;
import io.lana.libman.support.security.AuthFacade;
import io.lana.libman.support.security.AuthUser;
import io.lana.libman.support.ui.UIFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TaggedCrudController<T extends TaggedEntity> {
    @Autowired
    protected UIFacade ui;

    @Autowired
    protected AuthFacade<AuthUser> auth;

    protected final TaggedRepo<T> repo;

    protected abstract String getName();

    protected abstract String getAuthority();

    protected String getViewDir() {
        return getName().toLowerCase(Locale.ENGLISH);
    }

    @GetMapping
    public ModelAndView index(@RequestParam(value = "query", required = false) String query, Pageable pageable) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLike("%" + query + "%", pageable);
        return new ModelAndView("/library/tag/index", Map.of(
                "data", page,
                "title", getName(),
                "authority", getAuthority()
        ));
    }

    @GetMapping("delete")
    public ModelAndView confirmDelete(@RequestParam String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/tag/delete", Map.of(
                "entity", entity,
                "title", getName()
        ));
    }

    @PostMapping("delete")
    public ModelAndView delete(@RequestParam String id, @RequestHeader String referer) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getBooksCount() > 0) {
            ui.toast("Item have update during delete, please try again").warning();
        } else {
            repo.delete(entity);
            ui.toast("Item delete succeed").success();
        }
        return new ModelAndView("redirect:" + referer);
    }

    @PostMapping("force-delete")
    public ModelAndView forceDelete(@RequestParam String id, @RequestHeader String referer) {
        auth.requireAnyAuthorities("ADMIN", "FORCE");
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repo.delete(entity);
        ui.toast("Item delete succeed").success();
        return new ModelAndView("redirect:" + referer);
    }
}
