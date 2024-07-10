package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.controllers.handler.GenreExceptionHandler;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
@GenreExceptionHandler
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/api/v1/genre")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GenreDto> list() {
        return genreService.findAll();
    }
}