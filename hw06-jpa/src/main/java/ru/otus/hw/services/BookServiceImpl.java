package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, long genreId) {
        final var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        final var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));

        final var book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, long genreId) {
        if (id == 0L) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(id));
        }

        final var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        final var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));

        final var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
