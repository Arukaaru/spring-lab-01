
package kz.iitu.springlab01.catalog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {

        return repository.findAll().stream()
                .filter(book -> author == null ||
                        book.author().toLowerCase()
                                .contains(author.toLowerCase()))
                .sorted((a, b) ->
                        Long.compare(a.id(), b.id()))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@bookRepository.isOwner(#p0, authentication.name)"
    )
    public Optional<Book> findForEdit(long id) {
        return repository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Book create(Book book) {

        String owner = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return repository.save(
                new Book(
                        null,
                        book.title(),
                        book.author(),
                        book.year(),
                        owner
                )
        );
    }

    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@bookRepository.isOwner(#p0, authentication.name)"
    )
    public Optional<Book> replace(long id, Book book) {

        return repository.findById(id)
                .map(existing -> repository.save(
                        new Book(
                                id,
                                book.title(),
                                book.author(),
                                book.year(),
                                existing.owner()
                        )
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}
