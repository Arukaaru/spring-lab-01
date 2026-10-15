
package kz.iitu.springlab01.web;

import kz.iitu.springlab01.catalog.Book;
import kz.iitu.springlab01.catalog.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> list(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "10") int limit) {

        return service.findAll(author)
                .stream()
                .limit(limit)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> find(
            @PathVariable long id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(
            @RequestBody Book book) {

        Book saved = service.create(book);

        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> replace(
            @PathVariable long id,
            @RequestBody Book book) {

        return service.replace(id, book)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id) {

        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public BookStats stats() {

        List<Book> books = service.findAll(null);

        Integer earliestYear = books.stream()
                .map(Book::year)
                .min(Integer::compareTo)
                .orElse(null);

        Integer latestYear = books.stream()
                .map(Book::year)
                .max(Integer::compareTo)
                .orElse(null);

        return new BookStats(
                books.size(),
                earliestYear,
                latestYear
        );
    }

    public record BookStats(
            int count,
            Integer earliestYear,
            Integer latestYear
    ) {}

}
