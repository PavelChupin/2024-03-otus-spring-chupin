package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph("otus-book-lazy-entity-graph")
    List<Book> findAll();

    @Override
    @EntityGraph("otus-book-lazy-entity-graph")
    Optional<Book> findById(Long id);
}