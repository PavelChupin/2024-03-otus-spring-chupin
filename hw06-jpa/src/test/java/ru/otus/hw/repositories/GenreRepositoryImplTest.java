package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
@Import(GenreRepositoryImpl.class)
class GenreRepositoryImplTest {

    private static final long GENRE_ID = 1L;

    @Autowired
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Должен вернуть все жанры")
    @Test
    void findAll() {
        final TypedQuery<Genre> query = em.getEntityManager()
                .createQuery("select gn from Genre gn", Genre.class);

        final List<Genre> expected = query.getResultList();
        final List<Genre> actual = genreRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен вернуть жанр по его id")
    @Test
    void findById() {
        final Genre expected = em.find(Genre.class, GENRE_ID);
        final Optional<Genre> actual = genreRepository.findById(GENRE_ID);

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }
}