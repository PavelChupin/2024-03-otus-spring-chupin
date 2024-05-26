package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private static Book book;

    @BeforeAll
    static void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("BookTitle_1");
        book.setAuthor(new Author(1L, "Author_1"));
        book.setGenre(new Genre(1L, "Genre_1"));
    }

    @DisplayName("Должен вернуть книгу по id")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        final Book expected = book;
        final Optional<Book> actual = bookService.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);

        final Book bk = actual.get();

        assertThat(bk.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(bk.getAuthor().getFullName()).isEqualTo(expected.getAuthor().getFullName());
        assertThat(bk.getGenre()).isEqualTo(expected.getGenre());
        assertThat(bk.getGenre().getName()).isEqualTo(expected.getGenre().getName());
    }

    @DisplayName("кинуть исключение на переданный id = 0 книги при ее поиске")
    @Test
    void findByIdThrow() {
        assertThatThrownBy(() -> bookService.findById(0L)).isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Должен вернуть все книги")
    @Test
    void findAll() {
        final List<Book> expected = bookRepository.findAll();
        final List<Book> actual = bookService.findAll();

        assertThat(actual).isEqualTo(expected);
        actual.forEach(at ->
                {
                    final Book book = expected.stream()
                            .filter(ex -> ex.getId().equals(at.getId()))
                            .findFirst().get();

                    assertThat(at.getTitle()).isEqualTo(book.getTitle());
                    assertThat(at.getGenre()).isEqualTo(book.getGenre());
                    assertThat(at.getGenre().getName()).isEqualTo(book.getGenre().getName());
                    assertThat(at.getAuthor()).isEqualTo(book.getAuthor());
                    assertThat(at.getAuthor().getFullName()).isEqualTo(book.getAuthor().getFullName());
                }
        );
    }

    @DisplayName("Должен обновить книгу")
    @Test
    void update() {
        final String title = "NewTitle";
        final Book expected = book;
        expected.setTitle(title);
        final Book actual = bookService.update(expected.getId()
                , expected.getTitle()
                , expected.getAuthor().getId()
                , expected.getGenre().getId()
        );

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(actual.getGenre()).isEqualTo(expected.getGenre());
    }

    @DisplayName("Должен кинуть исключение на переданный id = 0 книги, автора или жанра при обновлении книги")
    @Test
    void updateByIdThrow() {
        assertThatThrownBy(() -> bookService.update(0L, "", 1L, 1L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> bookService.update(1L, "", 0L, 1L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> bookService.update(1L, "", 1L, 0L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Должен добавить новую книгу")
    @Test
    void insert() {
        final Author author = authorRepository.findAll().get(0);
        final Genre genre = genreRepository.findAll().get(0);
        final String title = "NewTitle";
        final Book expected = bookService.insert(title, author.getId(), genre.getId());

        final Optional<Book> actual = bookRepository.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);

        final Book bk = actual.get();

        assertThat(bk.getTitle()).isEqualTo(expected.getTitle());
        assertThat(bk.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(bk.getGenre()).isEqualTo(expected.getGenre());
    }

    @DisplayName("Должен кинуть исключение на переданный id = 0 автора или жанра при добавлении книги")
    @Test
    void insertByIdThrow() {
        assertThatThrownBy(() -> bookService.insert("", 0L, 1L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> bookService.insert("", 1L, 0L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
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

    @DisplayName("Должен кинуть исключение на переданный id = 0 книги при удалении книги")
    @Test
    void deleteByIdThrow() {
        assertThatThrownBy(() -> bookService.deleteById(0L)).isExactlyInstanceOf(EntityNotFoundException.class);
    }
}