package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageBookController {

    @GetMapping("/list")
    public String listBooksPage(Model model) {
        return "list";
    }

    @GetMapping("/create/book")
    public String createBookPage(Model model) {
//        final List<GenreDto> genreDtos = genreService.findAll();
//        final List<AuthorDto> authorDtos = authorService.findAll();
//        model.addAttribute("genres", genreDtos);
//        model.addAttribute("authors", authorDtos);
        return "create";
    }

    @GetMapping("/edit/book/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        //    final BookDto bookDto = bookService.findById(id);
//        final List<GenreDto> genreDtos = genreService.findAll();
//        final List<AuthorDto> authorDtos = authorService.findAll();

        //      model.addAttribute("book", bookDto);
//        model.addAttribute("genres", genreDtos);
//        model.addAttribute("authors", authorDtos);

        return "edit";
    }

    @GetMapping("/delete/book/{id}")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        //   final BookDto bookDto = bookService.findById(id);
        // model.addAttribute("book", bookDto);
        return "delete";
    }
}