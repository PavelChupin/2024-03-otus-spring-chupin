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
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

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
                .map(this::getBookDtoByBook)
                .collect(Collectors.toList());

        model.addAttribute("books", bookDtos);
        return "list";
    }

    @GetMapping("/create/book")
    public String createBookPage(Model model) {
        final List<GenreDto> genreDtos = genreService.findAll().stream()
                .map(this::getGenreDtoByGenre)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authorService.findAll().stream()
                .map(this::getAuthorDtoByAuthor)
                .collect(Collectors.toList());
        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);
        return "create";
    }

    @GetMapping("/edit/book/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        final Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        final BookDto bookDto = getBookDtoByBook(book);
        final List<GenreDto> genreDtos = genreService.findAll().stream()
                .map(this::getGenreDtoByGenre)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authorService.findAll().stream()
                .map(this::getAuthorDtoByAuthor)
                .collect(Collectors.toList());

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);
        return "edit";
    }

    @GetMapping("/delete/book/{id}")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        final Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        final BookDto bookDto = getBookDtoByBook(book);

        model.addAttribute("book", bookDto);
        return "delete";
    }

    @PostMapping("/edit/book/{id}")
    public String editBook(@Valid BookUpdateDto bookUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/edit/book/%d", bookUpdateDto.getId());
        }

        bookService.update(bookUpdateDto.getId()
                , bookUpdateDto.getTitle()
                , bookUpdateDto.getAuthorId()
                , bookUpdateDto.getGenreId());

        return "redirect:/list";
    }

    @PostMapping("/delete/book/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/list";
    }

    @PostMapping("/create/book")
    public String createBook(@Valid BookCreateDto bookCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/create/book";
        }
        bookService.create(bookCreateDto.getTitle(), bookCreateDto.getAuthorId(), bookCreateDto.getGenreId());
        return "redirect:/list";
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

    private GenreDto getGenreDtoByGenre(Genre genre) {
        final GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());


        return genreDto;
    }

    private AuthorDto getAuthorDtoByAuthor(Author author) {
        final AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFullName(author.getFullName());

        return authorDto;
    }
}