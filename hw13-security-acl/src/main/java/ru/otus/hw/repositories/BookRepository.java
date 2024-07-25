package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph("otus-book-lazy-entity-graph")
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

//    @Override
//    @EntityGraph("otus-book-lazy-entity-graph")
//    @PostAuthorize("hasPermission(returnObject, 'READ')")
//    Optional<Book> findById(Long id);

    @Override
    @EntityGraph("otus-book-lazy-entity-graph")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Book getById(Long id);

    @Override
    @PreAuthorize("hasPermission('DELETE')")
    void deleteById(Long id);

    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    Book save(@Param("book") Book book);
}