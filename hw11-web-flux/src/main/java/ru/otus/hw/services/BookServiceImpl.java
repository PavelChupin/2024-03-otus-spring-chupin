package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mapper.BookMapper;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(BookMapper::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found".formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(BookMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    @Transactional
    public Mono<BookDto> create(BookCreateDto bookCreateDto) {
        final var author = authorRepository.findById(bookCreateDto.getAuthorId()).toFuture().join();
                //.orElseThrow(() -> new NotFoundException("Author with id %s not found"
                  //      .formatted(bookCreateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookCreateDto.getGenreId()).toFuture().join();
               // .orElseThrow(() -> new NotFoundException("Genre with id %s not found"
                   //     .formatted(bookCreateDto.getGenreId())));

        final var book = BookMapper.toModelCreate(bookCreateDto.getTitle(), author, genre);

        return bookRepository.save(book).map(BookMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<BookDto> update(BookUpdateDto bookUpdateDto) {
        final var author = authorRepository.findById(bookUpdateDto.getAuthorId()).toFuture().join();
               // .orElseThrow(() -> new NotFoundException("Author with id %s not found"
               //         .formatted(bookUpdateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookUpdateDto.getGenreId()).toFuture().join();
               // .orElseThrow(() -> new NotFoundException("Genre with id %s not found"
                //        .formatted(bookUpdateDto.getGenreId())));
        final var book = bookRepository.findById(bookUpdateDto.getId()).toFuture().join();
               // .orElseThrow(() -> new NotFoundException("Book with id %s not found"
                //        .formatted(bookUpdateDto.getId())));
        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book).map(BookMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id).block();
    }
}