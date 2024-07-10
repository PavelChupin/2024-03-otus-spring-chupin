package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.handler.BookExceptionHandler;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

@RequiredArgsConstructor
@RestController
@BookExceptionHandler
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/api/v1/book")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookDto> list() {
        return bookService.findAll();
    }

    @GetMapping(value = "/api/v1/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> bookById(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @PutMapping(value = "/api/v1/book/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> update(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }

    @PostMapping(value = "/api/v1/book",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.create(bookCreateDto);
    }

    @DeleteMapping("/api/v1/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        bookService.deleteById(id);
    }
}