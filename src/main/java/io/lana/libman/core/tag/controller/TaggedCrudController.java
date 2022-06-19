package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.TaggedEntity;
import io.lana.libman.core.tag.repo.TaggedRepo;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import io.lana.libman.support.security.AuthFacade;
import io.lana.libman.support.security.AuthUser;
import io.lana.libman.support.ui.UIFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TaggedCrudController<T extends TaggedEntity, B extends AuditableEntity & Named> {
    @Autowired
    protected UIFacade ui;

    @Autowired
    protected AuthFacade<AuthUser> auth;

    protected final TaggedRepo<T> repo;

    protected abstract String getName();

    protected abstract String getAuthority();

    protected abstract Page<B> getRelationsPage(String id, String query, Pageable pageable);

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query, Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var page = getRelationsPage(id, query, pageable);
        return new ModelAndView("/library/tag/detail", Map.of(
                "entity", entity,
                "data", page,
                "title", getName()
        ));
    }

    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String query, Pageable pageable) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLikeIgnoreCase("%" + query + "%", pageable);
        return new ModelAndView("/library/tag/index", Map.of(
                "data", page,
                "title", getName(),
                "authority", getAuthority()
        ));
    }

    @GetMapping("{id}/delete")
    public ModelAndView confirmDelete(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/tag/delete", Map.of(
                "entity", entity,
                "title", getName(),
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    public ModelAndView delete(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getBooksCount() > 0) {
            ui.toast("Item have update during delete, please try again").warning();
        } else {
            repo.delete(entity);
            ui.toast("Item delete succeed").success();
        }
        return new ModelAndView("redirect:" + getIndexUri());
    }

    @PostMapping("{id}/force-delete")
    public ModelAndView forceDelete(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", "FORCE");
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repo.delete(entity);
        ui.toast("Item delete succeed").success();
        return new ModelAndView("redirect:" + getIndexUri());
    }
}
