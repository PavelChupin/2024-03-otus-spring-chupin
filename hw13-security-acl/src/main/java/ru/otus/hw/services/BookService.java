package ru.otus.hw.services;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {
    Book findById(Long id);

    List<Book> findAll();

    Book create(BookCreateDto bookCreateDto);

    Book update(Book book, String title, Long authorId, Long genreId);

    void delete(Book book);
}
