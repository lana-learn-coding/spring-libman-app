package io.lana.libman.core.user;

import io.lana.libman.core.file.ImageService;
import io.lana.libman.core.reader.Reader;
import io.lana.libman.core.reader.ReaderRepo;
import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

@Validated
@Controller
@RequestMapping("/authorities/users")
@RequiredArgsConstructor
class UserController {
    private final UserRepo repo;

    private final ReaderRepo readerRepo;

    private final PasswordEncoder passwordEncoder;

    private final ImageService imageService;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_READ')")
    public ModelAndView detail(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/user/detail", Map.of(
                "entity", entity
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String type, Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = repo.findAllByQuery(query, StringUtils.equals(type, Authorities.LIBRARIAN), pageable);
        return new ModelAndView("/auth/user/index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/user/edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_CREATE')")
    public ModelAndView create() {
        return new ModelAndView("/auth/user/edit", Map.of(
                "entity", new User(),
                "edit", false
        ));
    }

    @PostMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_UPDATE')")
    public ModelAndView update(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") User user,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (StringUtils.equalsIgnoreCase(user.getId(), Authorities.User.SYSTEM)) {
            ui.toast("Cannot update default system user").error();
            return new ModelAndView("redirect:/authorities/users");
        }

        final var entity = repo.findById(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "avatar")) {
            final var image = imageService.crop(file, 128, 128);
            user.setAvatar(imageService.save(image).getUri());
        }

        if (repo.existsByUsernameIgnoreCaseAndIdNot(user.getUsername(), user.getId())) {
            bindingResult.rejectValue("username", "", "The username was already taken");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/user/edit", Map.of(
                    "entity", user,
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        user.setEmail(entity.getEmail());
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(entity.getPassword());
        }

        repo.save(user);
        redirectAttributes.addFlashAttribute("highlight", user.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("User updated successfully").success();
        return new ModelAndView("redirect:/authorities/users");
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_CREATE')")
    public ModelAndView create(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") User user,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (StringUtils.equalsIgnoreCase(user.getId(), Authorities.User.SYSTEM)) {
            ui.toast("Cannot update default system user").error();
            return new ModelAndView("redirect:/authorities/users");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            bindingResult.rejectValue("password", "password.required", "Please provide a new password");
        }

        if (repo.existsByUsernameIgnoreCaseAndIdNot(user.getUsername(), user.getId())) {
            bindingResult.rejectValue("username", "", "The username was already taken");
        }

        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "avatar")) {
            final var image = imageService.crop(file, 128, 128);
            user.setAvatar(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/user/edit", Map.of(
                    "entity", user,
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
        redirectAttributes.addFlashAttribute("highlight", user.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        ui.toast("User created successfully").success();
        return new ModelAndView("redirect:/authorities/users");
    }

    @PostMapping(path = "{id}/link-reader")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_UPDATE')")
    public ModelAndView linkReader(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (StringUtils.equalsIgnoreCase(id, Authorities.User.SYSTEM)) {
            ui.toast("Cannot update default system user").error();
            return new ModelAndView("redirect:/authorities/users");
        }

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.isReader()) {
            ui.toast("User was already a reader").warning();
            return new ModelAndView("redirect:/authorities/users");
        }

        final var reader = new Reader();
        reader.setAccount(entity);
        readerRepo.save(reader);

        entity.setUpdatedAt(Instant.now());
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("User linked to new reader account successfully").success();
        return new ModelAndView("redirect:/authorities/users");
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_DELETE')")
    public ModelAndView confirmDelete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/user/delete", Map.of(
                "entity", entity,
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_DELETE')")
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (StringUtils.equalsIgnoreCase(entity.getId(), Authorities.User.SYSTEM)) {
            ui.toast("Cannot delete default system user").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/authorities/users");
        }

        if (entity.isReader() && entity.getReader().getBorrowingBooksCount() > 0) {
            final var isInternal = entity.isInternal();
            final var modelAndView = revoke(entity, redirectAttributes);
            if (isInternal) {
                ui.toast("User is borrowing book. Revoke to normal reader instead of delete").warning();
            } else {
                ui.toast("User is borrowing book. Cannot delete").error();
            }
            return modelAndView;
        }

        if (entity.isReader()) readerRepo.delete(entity.getReader());
        repo.delete(entity);
        ui.toast("Reader delete succeed").success();
        return new ModelAndView("redirect:/authorities/users");
    }

    @PostMapping("{id}/revoke")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER_DELETE')")
    public ModelAndView revoke(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (StringUtils.equalsIgnoreCase(entity.getId(), Authorities.User.SYSTEM)) {
            ui.toast("Cannot update default system user").error();
            return new ModelAndView("redirect:/authorities/users");
        }

        return revoke(entity, redirectAttributes);
    }

    private ModelAndView revoke(User entity, RedirectAttributes redirectAttributes) {
        entity.setRoles(new HashSet<>());
        repo.save(entity);
        redirectAttributes.addFlashAttribute("highlight", entity.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("User permissions revoked successfully").success();
        return new ModelAndView("redirect:/authorities/users");
    }
}
