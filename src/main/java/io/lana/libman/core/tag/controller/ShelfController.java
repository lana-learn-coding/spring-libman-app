package io.lana.libman.core.tag.controller;

import io.lana.libman.core.book.Book;
import io.lana.libman.core.book.repo.BookRepo;
import io.lana.libman.core.tag.Shelf;
import io.lana.libman.core.tag.repo.ShelfRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/library/tags/shelf")
class ShelfController extends TaggedCrudController<Shelf, Book> {
    private final BookRepo bookRepo;

    protected ShelfController(ShelfRepo repo, BookRepo bookRepo) {
        super(repo);
        this.bookRepo = bookRepo;
    }

    @Override
    protected String getName() {
        return "Shelf";
    }

    @Override
    protected String getAuthority() {
        return "SHELF";
    }

    @Override
    protected Page<Book> getRelationsPage(String id, String query, Pageable pageable) {
        return StringUtils.isBlank(query)
                ? bookRepo.findAllByShelfId(id, pageable)
                : bookRepo.findAllByShelfIdAndInfoTitleLike(id, "%" + query + "%", pageable);
    }

    @Override
    @PostMapping(value = "{id}/force-delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView forceDelete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        auth.requireAnyAuthorities("ADMIN", "FORCE");
        auth.requireAnyAuthorities("ADMIN", getAuthority() + "_DELETE");

        final var entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (StringUtils.equalsIgnoreCase(entity.getId(), "DEFAULT")) {
            ui.toast("Can't delete default item").error();
            redirectAttributes.addAttribute("sort", "updatedAt,desc");
            return new ModelAndView("redirect:" + getIndexUri());
        }

        final var storage = Shelf.storage();
        entity.getBooks().forEach(book -> {
            book.setShelf(storage);
            bookRepo.save(book);
        });

        repo.delete(entity);
        ui.toast("Item delete succeed").success();
        return new ModelAndView("redirect:" + getIndexUri());
    }
}
