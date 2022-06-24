package io.lana.libman.core.user.role;

import io.lana.libman.support.data.ModelUtils;
import io.lana.libman.support.data.NamedEntity;
import io.lana.libman.support.security.AuthFacade;
import io.lana.libman.support.security.AuthUser;
import io.lana.libman.support.ui.UIFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AuthoritiesCrudController<T extends NamedEntity> {
    protected final AuthoritiesRepo<T> repo;

    protected final Class<T> clazz = ModelUtils.getGenericType(getClass());

    @Autowired
    protected UIFacade ui;

    @Autowired
    protected AuthFacade<AuthUser> auth;

    protected abstract String getName();

    protected abstract String getAuthority();

    protected abstract Collection<? extends NamedEntity> extractRelations(T entity);

    protected String getIndexUri() {
        return "/authorities/" + getName().toLowerCase(Locale.ENGLISH);
    }

    protected String getEditFormView() {
        return "/auth/role/edit";
    }

    protected T newModelInstance() {
        return ModelUtils.construct(clazz);
    }

    @GetMapping("{id}/detail")
    public ModelAndView detail(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_READ");
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/role/detail", Map.of(
                "entity", entity,
                "title", getName(),
                "relations", extractRelations(entity)
        ));
    }

    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String query, Pageable pageable) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_READ");
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLikeIgnoreCase("%" + query + "%", pageable);

        return new ModelAndView("/auth/role/index", Map.of(
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
    public ModelAndView update(@Validated @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_UPDATE");
        if (!repo.existsById(entity.getId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List.of(Authorities.ADMIN, Authorities.FORCE, Authorities.LIBRARIAN)
                .forEach(name -> {
                    if (entity.getId().equalsIgnoreCase(name) && !entity.getName().equals(name)) {
                        bindingResult.rejectValue("name", "", "Cannot change name of default item");
                    }
                });

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView(getEditFormView(), Map.of(
                    "entity", entity,
                    "title", getName(),
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        entity.setName(StringUtils.upperCase(entity.getName()));
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        ui.toast("Item successfully updated").success();
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        return new ModelAndView("redirect:" + getIndexUri());
    }

    @GetMapping("create")
    public ModelAndView create() {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        final var entity = newModelInstance();
        return new ModelAndView(getEditFormView(), Map.of(
                "entity", entity,
                "title", getName(),
                "edit", false
        ));
    }

    @GetMapping("autocomplete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> autocomplete(@RequestParam(required = false, name = "q") String query) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_READ");
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
    public ModelAndView create(@Validated @ModelAttribute("entity") T entity,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_CREATE");
        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView(getEditFormView(), Map.of(
                    "entity", entity,
                    "title", getName(),
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        entity.setName(StringUtils.upperCase(entity.getName()));
        entity.setId(entity.getName());
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        ui.toast("Item successfully created").success();
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        return new ModelAndView("redirect:" + getIndexUri());
    }

    @GetMapping("{id}/delete")
    public ModelAndView confirmDelete(@PathVariable String id) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/role/delete", Map.of(
                "entity", entity,
                "title", getName(),
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (StringUtils.equalsAnyIgnoreCase(entity.getId(), Authorities.ADMIN, Authorities.FORCE, Authorities.LIBRARIAN)) {
            ui.toast("Can't delete default item").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + getIndexUri());

        }
        repo.delete(entity);
        ui.toast("Item delete succeed").success();
        return new ModelAndView("redirect:" + getIndexUri());
    }
}
