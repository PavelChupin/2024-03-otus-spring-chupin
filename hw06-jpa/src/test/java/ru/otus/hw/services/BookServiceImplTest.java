package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис по работе с книгами")
@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Должен вернуть книгу по id")
    @Test
    void findById() {
        final Book expected = getBook();
        final Optional<Book> actual = bookService.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @DisplayName("Должен вернуть все книги")
    @Test
    void findAll() {
        final List<Book> expected = bookRepository.findAll();
        final List<Book> actual = bookService.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен обновить книгу")
    @Test
    void update() {
        final String title = "NewTitle";
        final Book expected = getBook();
        expected.setTitle(title);
        final Book actual = bookService.update(expected.getId(), expected.getTitle(), expected.getAuthor().getId(), expected.getGenre().getId());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен добавил новую книгу")
    @Test
    void insert() {
        final Author author = authorRepository.findAll().get(0);
        final Genre genre = genreRepository.findAll().get(0);
        final String title = "NewTitle";
        final Book expected = bookService.insert(title, author.getId(), genre.getId());

        final Optional<Book> actual = bookRepository.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @DisplayName("Должен удалить книгу по id")
    @Test
    void deleteById() {
        final List<Book> expected = bookRepository.findAll();

        bookService.deleteById(expected.get(0).getId());
        expected.remove(0);

        final List<Book> actual = bookRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }


    private Book getBook() {
        return bookRepository.findAll().get(0);
    }
}