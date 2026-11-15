
package kz.iitu.springlab01.web;

import kz.iitu.springlab01.catalog.Book;
import kz.iitu.springlab01.catalog.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService service;

    public BookPageController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) String author,
            Model model,
            Authentication authentication) {

        var books = service.findAll(author);

        boolean isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        "ROLE_ADMIN".equals(a.getAuthority()));

        String username = authentication.getName();

        Set<Long> editableBookIds = books.stream()
                .filter(book ->
                        isAdmin || username.equals(book.owner()))
                .map(Book::id)
                .collect(Collectors.toSet());

        model.addAttribute("books", books);
        model.addAttribute("editableBookIds", editableBookIds);
        model.addAttribute("author", author);
        model.addAttribute("title", "Catalogue");

        return "books/list";
    }

    @PostMapping
    public String create(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam int year,
            RedirectAttributes redirect) {

        Book saved = service.create(
                new Book(null, title, author, year)
        );

        redirect.addFlashAttribute(
                "message",
                "Book «" + saved.title() + "» has been added"
        );

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable long id,
            Model model) {

        Book book = service.findForEdit(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND
                        )
                );

        model.addAttribute("book", book);

        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable long id,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam int year,
            RedirectAttributes redirect) {

        Book updated = service.replace(
                id,
                new Book(id, title, author, year)
        ).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND
                )
        );

        redirect.addFlashAttribute(
                "message",
                "Book «" + updated.title()
                        + "» has been updated"
        );

        return "redirect:/books";
    }
}
