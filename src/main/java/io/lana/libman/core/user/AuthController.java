package io.lana.libman.core.user;

import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookInfoRepo;
import io.lana.libman.core.reader.ReaderRepo;
import io.lana.libman.core.services.mail.MailService;
import io.lana.libman.core.services.mail.MailTemplate;
import io.lana.libman.core.user.dto.ChangePasswordDto;
import io.lana.libman.core.user.dto.ProfileUpdateDto;
import io.lana.libman.core.user.dto.ResetPasswordDto;
import io.lana.libman.support.security.AuthFacade;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Validated
@AllArgsConstructor
class AuthController {
    private final ReaderRepo readerRepo;

    private final BookBorrowRepo borrowRepo;

    private final BookInfoRepo bookInfoRepo;

    private final UserRepo userRepo;

    private final AuthFacade<User> auth;

    private final UIFacade ui;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final UserTokenService userTokenService;

    @GetMapping("/logout")
    public String logout() {
        return "/auth/logout";
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/reset-password")
    public ModelAndView requestResetPassword() {
        return new ModelAndView("/auth/pre-reset-password", Map.of("email", ""));
    }

    @PostMapping("/reset-password")
    public ModelAndView requestResetPassword(@RequestParam String email) {
        final var user = userRepo.findUserForAuth(email);
        if (user.isPresent()) {
            mailService.sendAsync(MailTemplate.changePassword()
                    .subject("Reset your password")
                    .to(user.get().getEmail())
                    .link(userTokenService.createResetPasswordLink(user.get())));
            ui.toast("Please check your mail for resetting password").success();
            return new ModelAndView("redirect:/login");
        }
        ui.toast("The email could not be found").error();
        return new ModelAndView("/auth/pre-reset-password", Map.of("email", email));
    }

    @GetMapping("/reset-password/{token}")
    public ModelAndView resetPassword(@PathVariable String token) {
        final var user = userTokenService.getUserByToken(token);
        if (user.isEmpty()) {
            ui.toast("Expired reset token. Please try again").error();
            return new ModelAndView("redirect:/reset-password");
        }
        return new ModelAndView("/auth/reset-password", Map.of("entity", new ResetPasswordDto()));
    }

    @PostMapping("/reset-password/{token}")
    public ModelAndView resetPassword(@PathVariable String token,
                                      @Validated @ModelAttribute("entity") ResetPasswordDto dto, BindingResult bindingResult) {
        final var user = userTokenService.getUserByToken(token);
        if (user.isEmpty()) {
            ui.toast("Expired reset token. Please try again").error();
            return new ModelAndView("redirect:/reset-password");
        }

        if (!StringUtils.equals(dto.getPassword(), dto.getRetypePassword())) {
            bindingResult.rejectValue("password", "", "Retype password not match");
            bindingResult.rejectValue("retypePassword", "", "Retype password not match");
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/reset-password", Map.of("entity", dto));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        user.get().setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user.get());
        ui.toast("Password successfully changed").success();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/me")
    public ModelAndView me() {
        final var user = auth.requirePrincipal();
        return new ModelAndView("/auth/me", Map.of("user", user, "password", new ChangePasswordDto(), "profile", new ProfileUpdateDto(user)));
    }

    @GetMapping("/me/borrowing")
    public ModelAndView myBorrowing(@RequestParam(required = false) String query,
                                    @PageableDefault(30) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = auth.requirePrincipal();
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

    @GetMapping("/me/borrowing/history")
    public ModelAndView myBorrowingHistory(@RequestParam(required = false) String query,
                                           @PageableDefault(30) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = auth.requirePrincipal();
        if (!entity.isReader()) {
            return new ModelAndView("/auth/borrowing", Map.of(
                    "entity", entity,
                    "data", Page.empty(pageable)
            ));
        }

        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = borrowRepo.findAllReturnedByReaderIdAndQuery(entity.getReader().getId(), query, pageable);
        return new ModelAndView("/auth/history", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @PostMapping("/me/favour/{id}")
    public String favorite(@PathVariable String id, @RequestParam(required = false, defaultValue = "false") boolean remove,
                           @RequestHeader(required = false, defaultValue = "/home") String referer) {
        final var entity = auth.requirePrincipal();
        final var book = bookInfoRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entity.isReader()) {
            ui.toast("You are not a reader").error();
            return "redirect:" + referer;
        }
        final var reader = readerRepo.findById(entity.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (remove) reader.getFavorites().remove(book);
        else reader.getFavorites().add(book);
        readerRepo.save(reader);
        entity.setReader(reader);
        ui.toast("Book added to favorite list").success();
        return "redirect:" + referer;
    }

    @GetMapping("/me/favorites")
    public ModelAndView myFavorite(@RequestParam(required = false) String query,
                                   @PageableDefault(30) @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = auth.requirePrincipal();
        if (!entity.isReader()) {
            return new ModelAndView("/auth/favorites", Map.of(
                    "entity", entity,
                    "data", Page.empty(pageable)
            ));
        }

        final var page = StringUtils.isBlank(query)
                ? bookInfoRepo.findAllByFavourersIs(entity.getReader(), pageable)
                : bookInfoRepo.findAllByFavourersIsAndTitleLikeIgnoreCase(entity.getReader(), "%" + query + "%", pageable);
        return new ModelAndView("/auth/favorites", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @PostMapping("/me/change-password")
    public ModelAndView changePassword(@Validated @ModelAttribute("password") ChangePasswordDto dto, BindingResult bindingResult) {
        final var user = auth.requirePrincipal();

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
        final var user = auth.requirePrincipal();

        if (!StringUtils.equals(dto.getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/auth/me", Map.of("user", user, "password", new ChangePasswordDto(), "profile", dto));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        BeanUtils.copyProperties(dto, user);
        userRepo.save(user);
        ui.toast("Profile updated successfully.").success();
        return new ModelAndView("redirect:/me");
    }
}
