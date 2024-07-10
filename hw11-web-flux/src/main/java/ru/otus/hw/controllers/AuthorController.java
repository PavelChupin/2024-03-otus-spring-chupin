package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.controllers.handler.AuthorExceptionHandler;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RequiredArgsConstructor
@RestController
@AuthorExceptionHandler
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/api/v1/author")
    @ResponseStatus(HttpStatus.OK)
    public Flux<AuthorDto> list() {
        return authorService.findAll();
    }
}