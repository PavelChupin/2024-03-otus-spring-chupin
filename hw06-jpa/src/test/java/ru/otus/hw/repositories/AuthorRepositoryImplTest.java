package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import(AuthorRepositoryImpl.class)
class AuthorRepositoryImplTest {

    private static final long AUTHOR_ID = 1L;

    @Autowired
    private AuthorRepositoryImpl authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Должен вернуть всех авторов")
    @Test
    void findAll() {
        final TypedQuery<Author> query = em.getEntityManager()
                .createQuery("select at from Author at", Author.class);

        final List<Author> expected = query.getResultList();
        final List<Author> actual = authorRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен вернуть автора по id")
    @Test
    void findById() {
        final Author expected = em.find(Author.class, AUTHOR_ID);
        final Optional<Author> actual = authorRepository.findById(AUTHOR_ID);

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }
}