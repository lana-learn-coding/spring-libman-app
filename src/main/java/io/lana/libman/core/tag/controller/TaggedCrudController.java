package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.Tagged;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TaggedCrudController<T extends AuditableEntity & Tagged & Named, B extends AuditableEntity & Named> {
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

    protected String getEditFormView() {
        return "/library/tag/edit";
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

    @GetMapping("{id}/update")
    public ModelAndView update(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView(getEditFormView(), Map.of(
                "entity", entity,
                "title", getName(),
                "edit", true
        ));
    }


    @PostMapping(path = "{id}/update")
    public ModelAndView update(@PathVariable String id, @Valid @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");
        entity.setId(id);
        validateEntity(entity, bindingResult, id);
        return store(entity, bindingResult, redirectAttributes, id);
    }

    @GetMapping("create")
    public ModelAndView create() {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        final var entity = ModelUtils.construct(clazz);
        return new ModelAndView(getEditFormView(), Map.of(
                "entity", entity,
                "title", getName(),
                "edit", false
        ));
    }

    @GetMapping("autocomplete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> autocomplete(@RequestParam(required = false, name = "q") String query) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(Pageable.ofSize(6))
                : repo.findAllByNameLikeIgnoreCase("%" + query + "%", Pageable.ofSize(6));
        final var data = page.stream()
                .map(t -> Map.of("id", t.getId(), "text", t.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "results", data
        ));
    }

    @PostMapping(path = "create")
    public ModelAndView create(@Valid @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        validateEntity(entity, bindingResult, null);
        return store(entity, bindingResult, redirectAttributes, null);
    }

    protected ModelAndView store(T entity, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, String id) {
        final var isEdit = StringUtils.isNotBlank(id);
        if (isEdit && !repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView(getEditFormView(), Map.of(
                    "entity", entity,
                    "title", getName(),
                    "edit", isEdit
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());

        if (isEdit) {
            ui.toast("Item successfully updated").success();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + getIndexUri());
        }
        ui.toast("Item successfully created").success();
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
        if (StringUtils.equalsIgnoreCase(entity.getId(), "DEFAULT")) {
            ui.toast("Can't delete default item").error();
        } else if (entity.getBooksCount() > 0) {
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
        if (StringUtils.equalsIgnoreCase(entity.getId(), "DEFAULT")) {
            ui.toast("Can't delete default item").error();
            return new ModelAndView("redirect:" + getIndexUri());
        }

        repo.delete(entity);
        ui.toast("Item delete succeed").success();
        return new ModelAndView("redirect:" + getIndexUri());
    }

    protected void validateEntity(T entity, BindingResult bindingResult, String id) {
        if (StringUtils.isBlank(id)) {
            if (repo.existsByNameIgnoreCase(entity.getName())) {
                bindingResult.rejectValue("name", "name.exists", "The name was already taken");
            }
            return;
        }

        if (repo.existsByNameIgnoreCaseAndIdNot(entity.getName(), entity.getId())) {
            bindingResult.rejectValue("name", "name.exists", "The name was already taken");
        }
    }
}
