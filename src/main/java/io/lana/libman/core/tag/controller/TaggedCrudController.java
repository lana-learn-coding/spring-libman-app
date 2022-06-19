package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.TaggedEntity;
import io.lana.libman.core.tag.repo.TaggedRepo;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.ModelUtils;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TaggedCrudController<T extends TaggedEntity, B extends AuditableEntity & Named> {
    protected final TaggedRepo<T> repo;

    protected final Class<T> clazz = ModelUtils.getGenericType(getClass());

    @Autowired
    protected UIFacade ui;

    @Autowired
    protected AuthFacade<AuthUser> auth;

    protected abstract String getName();

    protected abstract String getAuthority();

    protected abstract Page<B> getRelationsPage(String id, String query, Pageable pageable);

    protected String getIndexUri() {
        return "/library/tags/" + getName().toLowerCase(Locale.ENGLISH);
    }

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
    public ModelAndView index(@RequestParam(required = false) String query, Pageable pageable,
                              RedirectAttributes redirectAttributes) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLikeIgnoreCase("%" + query + "%", pageable);

        return new ModelAndView("/library/tag/index", Map.of(
                "data", page,
                "title", getName(),
                "authority", getAuthority()
        ));
    }

    @GetMapping("{id}/update")
    public ModelAndView update(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/tag/edit", Map.of(
                "entity", entity,
                "title", getName(),
                "edit", true
        ));
    }


    @PostMapping("{id}/update")
    public ModelAndView update(@PathVariable String id, @Valid @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");

        entity.setId(id);
        if (repo.existsByNameIgnoreCaseAndIdNot(entity.getName(), entity.getId())) {
            bindingResult.rejectValue("name", "name.exists", "The name was already taken");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/tag/edit", Map.of(
                    "entity", entity,
                    "title", getName(),
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(entity);
        ui.toast("Item successfully created").success();
        redirectAttributes.addAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        return new ModelAndView("redirect:" + getIndexUri());
    }

    @GetMapping("create")
    public ModelAndView create() {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        final var entity = ModelUtils.construct(clazz);
        return new ModelAndView("/library/tag/edit", Map.of(
                "entity", entity,
                "title", getName(),
                "edit", false
        ));
    }

    @PostMapping("create")
    public ModelAndView create(@Valid @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        if (repo.existsByNameIgnoreCase(entity.getName())) {
            bindingResult.rejectValue("name", "name.exists", "The name was already taken");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/tag/edit", Map.of(
                    "entity", entity,
                    "title", getName(),
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(entity);
        ui.toast("Item successfully created").success();
        redirectAttributes.addAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        return new ModelAndView("redirect:" + getIndexUri());
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
