//package ru.otus.hw.repositories;
//
//import jakarta.persistence.EntityGraph;
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import ru.otus.hw.models.Book;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;
//
//@DisplayName("Репозиторий на основе Jpa для работы с книгами")
//@DataJpaTest
//class BookRepositoryTest {
//
//    private static final Long BOOK_ID = 1L;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private TestEntityManager em;
//
//    @DisplayName("Должен вернуть книгу по id")
//    @Test
//    void findByIdTest() {
//        final EntityGraph<?> entityGraph = em.getEntityManager().getEntityGraph("otus-book-lazy-entity-graph");
//        final Map<String, Object> properties = new HashMap<>();
//        properties.put("jakarta.persistence.fetchgraph", entityGraph);
//        final Book expected = em.getEntityManager().find(Book.class, BOOK_ID, properties);
//        final Optional<Book> actual = bookRepository.findById(BOOK_ID);
//
//        assertThat(actual).isPresent().get().isEqualTo(expected);
//        final Book actualBook = actual.get();
//        assertThat(actualBook.getTitle()).isEqualTo(expected.getTitle());
//        assertThat(actualBook.getGenre()).isEqualTo(expected.getGenre());
//        assertThat(actualBook.getGenre().getName()).isEqualTo(expected.getGenre().getName());
//        assertThat(actualBook.getAuthor()).isEqualTo(expected.getAuthor());
//        assertThat(actualBook.getAuthor().getFullName()).isEqualTo(expected.getAuthor().getFullName());
//    }
//
//    @DisplayName("Должен вернуть все книги")
//    @Test
//    void findAllTest() {
//        final TypedQuery<Book> query = em.getEntityManager()
//                .createQuery("select bk from Book bk", Book.class);
//        final EntityGraph<?> entityGraph = em.getEntityManager()
//                .createEntityGraph("otus-book-lazy-entity-graph");
//        query.setHint(FETCH.getKey(), entityGraph);
//
//        final List<Book> expected = query.getResultList();
//        final List<Book> actual = bookRepository.findAll();
//
//        assertThat(actual).isEqualTo(expected);
//        actual.forEach(actualBook ->
//                {
//                    final Book expectedBook = expected.stream()
//                            .filter(ex -> ex.getId().equals(actualBook.getId()))
//                            .findFirst().get();
//
//                    assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
//                    assertThat(actualBook.getGenre()).isEqualTo(expectedBook.getGenre());
//                    assertThat(actualBook.getGenre().getName()).isEqualTo(expectedBook.getGenre().getName());
//                    assertThat(actualBook.getAuthor()).isEqualTo(expectedBook.getAuthor());
//                    assertThat(actualBook.getAuthor().getFullName()).isEqualTo(expectedBook.getAuthor().getFullName());
//                }
//        );
//    }
//}