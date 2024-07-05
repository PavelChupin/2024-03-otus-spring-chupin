package ru.otus.hw.controllers.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.exceptions.NotFoundException;

@RestControllerAdvice(annotations = {BookExceptionHandler.class,
        GenreExceptionHandler.class,
        AuthorExceptionHandler.class})
@Slf4j
public class LibraryControllerHandlerException {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto exceptionHandler(Exception e) {
        log.error("Internal Server Error.", e);
        return new ErrorDto(e.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto methodArgumentNotValidExceptionHandler(Exception e) {
        log.warn("Bad Request.", e);
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFoundExceptionHandler(Exception e) {
        log.warn("Not Found.", e);
        return new ErrorDto(e.getMessage());
    }
}