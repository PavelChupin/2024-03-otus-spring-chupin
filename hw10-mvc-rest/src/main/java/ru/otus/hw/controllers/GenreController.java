package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.controllers.handler.GenreExceptionHandler;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@GenreExceptionHandler
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/api/v1/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> list() {
        return genreService.findAll();
    }
}