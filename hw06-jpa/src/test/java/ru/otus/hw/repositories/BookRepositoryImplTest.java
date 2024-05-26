package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import(BookRepositoryImpl.class)
class BookRepositoryImplTest {

    private static final long BOOK_ID = 1L;

    @Autowired
    private BookRepositoryImpl bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Должен вернуть книгу по id")
    @Test
    void findById() {
        final Book expected = em.find(Book.class, BOOK_ID);
        final Optional<Book> actual = bookRepository.findById(BOOK_ID);

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @DisplayName("Должен вернуть все книги")
    @Test
    void findAll() {
        final TypedQuery<Book> query = em.getEntityManager()
                .createQuery("select bk from Book bk", Book.class);

        final List<Book> expected = query.getResultList();
        final List<Book> actual = bookRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен сохранить книгу")
    @Test
    void save() {
        final List<Book> expected = getBooks();

        final TypedQuery<Genre> queryGenre = em.getEntityManager()
                .createQuery("select gn from Genre gn", Genre.class);

        final Genre genre = queryGenre.getResultList().get(0);

        final TypedQuery<Author> queryAuthor = em.getEntityManager()
                .createQuery("select at from Author at", Author.class);

        final Author author = queryAuthor.getResultList().get(0);

        final Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setGenre(genre);
        newBook.setAuthor(author);

        expected.add(newBook);
        bookRepository.save(newBook);
        final List<Book> actual = getBooks();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен удалить книгу по id")
    @Test
    void deleteById() {
        final List<Book> expected = getBooks();

        bookRepository.deleteById(expected.get(0).getId());
        expected.remove(0);

        final List<Book> actual = getBooks();
        assertThat(actual).isEqualTo(expected);
    }

    private List<Book> getBooks() {
        final TypedQuery<Book> queryBook = em.getEntityManager()
                .createQuery("select bk from Book bk", Book.class);

        return queryBook.getResultList();
    }
}