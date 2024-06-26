package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/list/api/v1", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> listBooksPage() {
        return bookService.findAll().stream()
                .map(this::getBookDtoByBook)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/edit/book/api/v1", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public BookDto editBook(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        final Book book = bookService.update(bookUpdateDto.getId()
                , bookUpdateDto.getTitle()
                , bookUpdateDto.getAuthorId()
                , bookUpdateDto.getGenreId());

        return getBookDtoByBook(book);
    }

    @PostMapping(value = "/create/api/v1", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        final Book book = bookService.create(bookCreateDto.getTitle(), bookCreateDto.getAuthorId(), bookCreateDto.getGenreId());
        return getBookDtoByBook(book);
    }

    @DeleteMapping("/delete/book/api/v1/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @ExceptionHandler({RuntimeException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<?> exceptionHandler(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("{\"error\": \"%s\"}", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(String.format("{\"error\": \"%s\"}", e.toString()));
    }

    private BookDto getBookDtoByBook(Book book) {
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