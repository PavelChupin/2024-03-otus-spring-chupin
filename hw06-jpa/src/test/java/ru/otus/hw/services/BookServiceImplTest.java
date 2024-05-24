package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
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

    private static Book book;

    @BeforeAll
    static void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("BookTitle_1");
        book.setAuthor(new Author(1, "Author_1"));
        book.setGenre(new Genre(1, "Genre_1"));
    }

    @DisplayName("Должен вернуть книгу по id")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        final Book expected = book;
        final Optional<Book> actual = bookService.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);
        Book bk = actual.get();
        assertThat(bk.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(bk.getGenre()).isEqualTo(expected.getGenre());
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
                            .filter(ex -> ex.getId() == at.getId())
                            .findFirst().get();

                    assertThat(at.getTitle()).isEqualTo(book.getTitle());
                    assertThat(at.getGenre()).isEqualTo(book.getGenre());
                    assertThat(at.getAuthor()).isEqualTo(book.getAuthor());
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

    @DisplayName("Должен добавить новую книгу")
    @Test
    void insert() {
        final Author author = authorRepository.findAll().get(0);
        final Genre genre = genreRepository.findAll().get(0);
        final String title = "NewTitle";
        final Book expected = bookService.insert(title, author.getId(), genre.getId());

        final Optional<Book> actual = bookRepository.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);
        actual.ifPresent(value ->
                assertThat(actual.get().getTitle()).isEqualTo(expected.getTitle()));
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
}