package io.lana.libman.core.reader;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.repo.BookBorrowRepo;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.file.ImageService;
import io.lana.libman.core.user.UserRepo;
import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.support.ui.UIFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Controller
@RequestMapping("/library/readers")
@RequiredArgsConstructor
class ReaderController {
    private final ReaderRepo repo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final BookBorrowRepo borrowRepo;

    private final BookRepo bookRepo;

    private final ImageService imageService;

    private final UIFacade ui;

    @GetMapping("{id}/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ')")
    public ModelAndView detail(@PathVariable String id, @RequestParam(required = false) String query,
                               @SortDefault(value = "dueDate") Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var page = StringUtils.isBlank(query)
                ? borrowRepo.findAllBorrowingByReaderId(id, pageable)
                : borrowRepo.findAllBorrowingByReaderIdAndQuery(id, "%" + query + "%", pageable);
        return new ModelAndView("/library/reader/detail", Map.of(
                "entity", entity,
                "data", page
        ));
    }


    @GetMapping("{id}/history")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ', 'BOOKBORROW_READ')")
    public ModelAndView history(@PathVariable String id, @RequestParam(required = false) String query,
                                @PageableDefault(5) @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        final var page = borrowRepo.findAllByReaderIdAndQuery(id, query, pageable);
        return new ModelAndView("/library/reader/history", Map.of(
                "entity", entity,
                "data", page
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ')")
    public ModelAndView index(@RequestParam(required = false) String query, @RequestParam(required = false) String email,
                              @SortDefault(value = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        query = StringUtils.isBlank(query) ? null : "%" + query + "%";
        email = StringUtils.isBlank(email) ? null : "%" + email + "%";

        final var page = StringUtils.isAllBlank(query, email)
                ? repo.findAll(pageable)
                : repo.findAllByQueryAndEmail(query, email, pageable);
        return new ModelAndView("/library/reader/index", Map.of("data", page));
    }

    @GetMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_UPDATE')")
    public ModelAndView update(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/reader/edit", Map.of(
                "entity", entity,
                "edit", true
        ));
    }

    @GetMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_CREATE')")
    public ModelAndView create() {
        return new ModelAndView("/library/reader/edit", Map.of(
                "entity", new Reader(),
                "edit", false
        ));
    }

    @PostMapping("{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_UPDATE')")
    public ModelAndView update(@PathVariable("id") String id,
                               @RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") Reader reader,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!id.equals(reader.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var user = reader.getAccount();

        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "avatar")) {
            final var image = imageService.crop(file, 128, 128);
            user.setAvatar(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/reader/edit", Map.of(
                    "entity", reader,
                    "edit", true
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        user.setId(id);
        user.setEmail(entity.getAccount().getEmail());
        user.setPhone(entity.getAccount().getPhone());
        user.setPassword(entity.getAccount().getPassword());
        user.setUsername(entity.getAccount().getUsername());
        userRepo.save(user);
        repo.save(reader);
        redirectAttributes.addFlashAttribute("highlight", reader.getId());
        redirectAttributes.addAttribute("sort", "updatedAt,desc");
        ui.toast("Reader updated successfully").success();
        return new ModelAndView("redirect:/library/readers");
    }

    @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_CREATE')")
    public ModelAndView create(@RequestPart(required = false) MultipartFile file,
                               @Validated @ModelAttribute("entity") Reader reader,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        final var user = reader.getAccount();

        if (Objects.nonNull(file) && imageService.validate(file, bindingResult, "avatar")) {
            final var image = imageService.crop(file, 128, 128);
            user.setAvatar(imageService.save(image).getUri());
        }

        if (bindingResult.hasErrors()) {
            final var model = new ModelAndView("/library/reader/edit", Map.of(
                    "entity", reader,
                    "edit", false
            ));
            model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            return model;
        }

        final var random = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(random));
        user.setId(reader.getId());
        userRepo.save(user);
        repo.save(reader);
        redirectAttributes.addFlashAttribute("highlight", reader.getId());
        redirectAttributes.addAttribute("sort", "createdAt,desc");
        ui.toast("Reader created successfully").success();
        return new ModelAndView("redirect:/library/readers");
    }

    @GetMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_DELETE')")
    public ModelAndView confirmDelete(@PathVariable String id) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ModelAndView("/library/reader/delete", Map.of(
                "entity", entity,
                "id", id
        ));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_DELETE')")
    public ModelAndView delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entity.getBorrowingBooksCount() > 0) {
            ui.toast("Reader is borrowing book. Cannot delete").error();
            return new ModelAndView("redirect:/library/readers");
        }

        if (StringUtils.equalsIgnoreCase(entity.getAccount().getId(), Authorities.User.SYSTEM)) {
            ui.toast("Cannot delete default system user").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:/library/readers");
        }

        repo.delete(entity);
        entity.getBorrowingBooks().forEach(borrow -> {
            final var book = borrow.getBook();
            book.setStatus(Book.Status.AVAILABLE);
            bookRepo.save(book);

            borrow.setReturned(true);
            borrow.setReturnDate(LocalDate.now());
            borrow.setTotalCost(0d);
            borrowRepo.save(borrow);
        });

        if (!entity.getAccount().isInternal()) userRepo.delete(entity.getAccount());
        ui.toast("Reader delete succeed").success();
        return new ModelAndView("redirect:/library/readers");
    }

    @GetMapping("autocomplete")
    @PreAuthorize("hasAnyAuthority('ADMIN','READER_READ')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> availableAutocomplete(@RequestParam(required = false, name = "q") String query) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(Pageable.ofSize(6))
                : repo.findAllByQuery("%" + query + "%", Pageable.ofSize(6));
        final var data = page.stream()
                .map(t -> Map.of("id", t.getId(), "text", t.getAccount().getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "results", data
        ));
    }
}
