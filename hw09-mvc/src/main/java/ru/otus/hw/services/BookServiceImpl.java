package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

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
    public BookDto findById(Long id) {
        if (id.equals(0L)) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(id));
        }

        return bookToDto(bookRepository.findById(id).get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::bookToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto create(BookCreateDto bookCreateDto) {
        if (bookCreateDto.getAuthorId().equals(0L)) {
            throw new EntityNotFoundException("Incorrect author id %d passed".formatted(bookCreateDto.getAuthorId()));
        }

        if (bookCreateDto.getGenreId().equals(0L)) {
            throw new EntityNotFoundException("Incorrect genre id %d passed".formatted(bookCreateDto.getGenreId()));
        }

        final var author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookCreateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(bookCreateDto.getGenreId())));

        final var book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return bookToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto bookUpdateDto) {
        if (bookUpdateDto.getId().equals(0L)) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(bookUpdateDto.getId()));
        }
        if (bookUpdateDto.getAuthorId().equals(0L)) {
            throw new EntityNotFoundException("Incorrect author id %d passed".formatted(bookUpdateDto.getAuthorId()));
        }
        if (bookUpdateDto.getGenreId().equals(0L)) {
            throw new EntityNotFoundException("Incorrect genre id %d passed".formatted(bookUpdateDto.getGenreId()));
        }
        final var author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookUpdateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookUpdateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(bookUpdateDto.getGenreId())));
        final var book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found"
                        .formatted(bookUpdateDto.getId())));
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return bookToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id.equals(0L)) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(id));
        }

        bookRepository.deleteById(id);
    }

    private BookDto bookToDto(Book book) {
        final BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        final AuthorDto authorDto = new AuthorDto();
        authorDto.setId(book.getAuthor().getId());
        authorDto.setFullName(book.getAuthor().getFullName());
        bookDto.setAuthorDto(authorDto);
        final GenreDto genreDto = new GenreDto();
        genreDto.setId(book.getGenre().getId());
        genreDto.setName(book.getGenre().getName());
        bookDto.setGenreDto(genreDto);

        return bookDto;
    }
}