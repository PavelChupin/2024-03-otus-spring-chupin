package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.controllers.handler.AuthorExceptionHandler;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@AuthorExceptionHandler
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/api/v1/author", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDto> list() {
        return authorService.findAll();
    }
}