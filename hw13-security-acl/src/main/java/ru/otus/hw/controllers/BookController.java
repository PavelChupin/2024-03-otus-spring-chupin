package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.mapper.AuthorMapper;
import ru.otus.hw.services.mapper.BookMapper;
import ru.otus.hw.services.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/list")
    public String listBooksPage(Model model) {
        final List<BookDto> bookDtos = bookService.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", bookDtos);

        return "list";
    }

    @GetMapping("/create/book")
    public String createBookPage(Model model) {
        final List<GenreDto> genreDtos = genreService.findAll().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authorService.findAll().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);

        return "create";
    }

    @GetMapping("/edit/book/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        final BookDto bookDto = BookMapper.toDto(bookService.findById(id));
        final List<GenreDto> genreDtos = genreService.findAll().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authorService.findAll().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);

        return "edit";
    }

    @GetMapping("/delete/book/{id}")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        final BookDto bookDto = BookMapper.toDto(bookService.findById(id));
        model.addAttribute("book", bookDto);

        return "delete";
    }

    @PostMapping("/edit/book/{id}")
    public String update(@Valid BookUpdateDto bookUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/edit/book/%d", bookUpdateDto.getId());
        }

        final var book = bookService.findById(bookUpdateDto.getId());

        bookService.update(book, bookUpdateDto.getTitle(), bookUpdateDto.getAuthorId(), bookUpdateDto.getGenreId());

        return "redirect:/list";
    }

    @PostMapping("/delete/book/{id}")
    public String delete(@PathVariable("id") Long id) {
        final var book = bookService.findById(id);
        bookService.delete(book);
        //bookService.deleteById(id);
        return "redirect:/list";
    }

    @PostMapping("/create/book")
    public String create(@Valid BookCreateDto bookCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/create/book";
        }
        bookService.create(bookCreateDto);

        return "redirect:/list";
    }
}