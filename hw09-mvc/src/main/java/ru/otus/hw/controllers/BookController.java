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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/list")
    public String listBooksPage(Model model) {
        final List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);

        return "list";
    }

    @GetMapping("/create/book")
    public String createBookPage(Model model) {
        final List<GenreDto> genreDtos = genreService.findAll();
        final List<AuthorDto> authorDtos = authorService.findAll();

        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);

        return "create";
    }

    @GetMapping("/edit/book/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        final BookDto bookDto = bookService.findById(id);
        final List<GenreDto> genreDtos = genreService.findAll();
        final List<AuthorDto> authorDtos = authorService.findAll();

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);

        return "edit";
    }

    @GetMapping("/delete/book/{id}")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        final BookDto bookDto = bookService.findById(id);
        model.addAttribute("book", bookDto);

        return "delete";
    }

    @PostMapping("/edit/book/{id}")
    public String update(@Valid BookUpdateDto bookUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/edit/book/%d", bookUpdateDto.getId());
        }

        bookService.update(bookUpdateDto);

        return "redirect:/list";
    }

    @PostMapping("/delete/book/{id}")
    public String delete(@PathVariable("id") Long id) {
        bookService.deleteById(id);
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