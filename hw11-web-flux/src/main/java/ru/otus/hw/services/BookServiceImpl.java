package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mapper.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(String id) {
        return BookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto create(BookCreateDto bookCreateDto) {
        final var author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author with id %s not found"
                        .formatted(bookCreateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() -> new NotFoundException("Genre with id %s not found"
                        .formatted(bookCreateDto.getGenreId())));

        final var book = BookMapper.toModelCreate(bookCreateDto.getTitle(), author, genre);

        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto bookUpdateDto) {
        final var author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author with id %s not found"
                        .formatted(bookUpdateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookUpdateDto.getGenreId())
                .orElseThrow(() -> new NotFoundException("Genre with id %s not found"
                        .formatted(bookUpdateDto.getGenreId())));
        final var book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %s not found"
                        .formatted(bookUpdateDto.getId())));
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}