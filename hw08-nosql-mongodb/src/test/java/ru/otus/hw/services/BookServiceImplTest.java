package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
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
    private CommentRepository commentRepository;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Должен вернуть книгу по id")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findByIdTest() {
        final Book expected = bookRepository.findAll().get(0);
        final Optional<Book> actual = bookService.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);

        final Book bk = actual.get();

        assertThat(bk.getTitle()).isEqualTo(expected.getTitle());
        assertThat(bk.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(bk.getAuthor().getFullName()).isEqualTo(expected.getAuthor().getFullName());
        assertThat(bk.getGenre()).isEqualTo(expected.getGenre());
        assertThat(bk.getGenre().getName()).isEqualTo(expected.getGenre().getName());
    }

    @DisplayName("Должен вернуть все книги")
    @Test
    void findAllTest() {
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
    void updateTest() {
        final String title = "NewTitle";
        final Book expected = bookRepository.findAll().get(0);
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
    void createTest() {
        final Author author = authorRepository.findAll().get(0);
        final Genre genre = genreRepository.findAll().get(0);
        final String title = "NewTitle";
        final Book expected = bookService.create(title, author.getId(), genre.getId());

        final Optional<Book> actual = bookRepository.findById(expected.getId());

        assertThat(actual).isPresent().get().isEqualTo(expected);

        final Book bk = actual.get();

        assertThat(bk.getTitle()).isEqualTo(expected.getTitle());
        assertThat(bk.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(bk.getGenre()).isEqualTo(expected.getGenre());
    }

    @DisplayName("Должен удалить книгу по id")
    @Test
    void deleteByIdTest() {
        final List<Book> expected = bookRepository.findAll();
        final String id = expected.get(0).getId();
        bookService.deleteById(id);
        expected.remove(0);

        final List<Book> actual = bookRepository.findAll();

        assertThat(actual).isEqualTo(expected);

        // Проверим что каскадная операция тоже сработала
        final List<Comment> comments = commentRepository.findAllByBookId(id);
        assertThat(comments).isEmpty();
    }
}