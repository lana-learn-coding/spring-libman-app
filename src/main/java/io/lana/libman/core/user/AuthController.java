package io.lana.libman.core.user;

import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.user.dto.ChangePasswordDto;
import io.lana.libman.core.user.dto.ProfileUpdateDto;
import io.lana.libman.support.security.AuthFacade;
import io.lana.libman.support.security.AuthUser;
import io.lana.libman.support.ui.UIFacade;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Validated
@AllArgsConstructor
class AuthController {
    private final BookBorrowRepo borrowRepo;

    private final UserRepo userRepo;

    private final AuthFacade<AuthUser> auth;

    private final UIFacade ui;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/logout")
    public String logout() {
        return "/auth/logout";
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/me")
    public ModelAndView me() {
        final var authUser = auth.requirePrincipal();
        final var user = userRepo.findById(authUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/auth/me", Map.of("user", user, "password", new ChangePasswordDto(), "profile", new ProfileUpdateDto(user)));
    }

    @GetMapping("/me/borrowing")
    public ModelAndView myBorrowing(@RequestParam(required = false) String query,
                                    @PageableDefault(30) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        final var authUser = auth.requirePrincipal();
        final var entity = userRepo.findById(authUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entity.isReader()) {
            return new ModelAndView("/auth/borrowing", Map.of(
                    "entity", entity,
                    "data", Page.empty(pageable)
            ));
        }

        final var page = StringUtils.isBlank(query)
                ? borrowRepo.findAllBorrowingByReaderId(entity.getReader().getId(), pageable)
                : borrowRepo.findAllBorrowingByReaderIdAndQuery(entity.getReader().getId(), "%" + query + "%", pageable);
        return new ModelAndView("/auth/borrowing", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @PostMapping("/me/change-password")
    public ModelAndView changePassword(@Validated @ModelAttribute("password") ChangePasswordDto dto, BindingResult bindingResult) {
        final var authUser = auth.requirePrincipal();
        final var user = userRepo.findById(authUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!StringUtils.equals(dto.getNewPassword(), dto.getRetypeNewPassword())) {
            bindingResult.rejectValue("newPassword", "", "Retype password not match");
            bindingResult.rejectValue("retypeNewPassword", "", "Retype password not match");
        }

        if (StringUtils.isBlank(dto.getOldPassword()) || !passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            bindingResult.rejectValue("oldPassword", "", "Wrong password!");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/me", Map.of("user", user, "password", dto, "profile", new ProfileUpdateDto(user)));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);
        ui.toast("Password successfully updated").success();
        return new ModelAndView("redirect:/me");
    }

    @PostMapping("/me/update-profile")
    public ModelAndView updateProfile(@Validated @ModelAttribute("profile") ProfileUpdateDto dto, BindingResult bindingResult) {
        final var authUser = auth.requirePrincipal();

        if (!StringUtils.equals(dto.getId(), authUser.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final var user = userRepo.findById(authUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/me", Map.of("user", user, "password", new ChangePasswordDto(), "profile", dto));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        BeanUtils.copyProperties(dto, user);
        userRepo.save(user);
        ui.toast("Profile updated successfully. Please re-login to view updated info").success();
        return new ModelAndView("redirect:/me");
    }
}
