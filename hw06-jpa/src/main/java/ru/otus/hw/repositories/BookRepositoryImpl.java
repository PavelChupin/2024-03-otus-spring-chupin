package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        final EntityGraph<?> entityGraph = em.getEntityGraph("otus-book-lazy-entity-graph");
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public List<Book> findAll() {
        final TypedQuery<Book> bookQuery = em.createQuery("select gr from Book gr", Book.class);
        final EntityGraph<?> entityGraph = em.createEntityGraph("otus-book-lazy-entity-graph");
        bookQuery.setHint(FETCH.getKey(), entityGraph);
        return bookQuery.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
        // Старый вариант
//        final Query query = em.createQuery("delete from Book where id =:id");
//        query.setParameter("id", id);
//        query.executeUpdate();
        // Предложенный на ревью
        final Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}