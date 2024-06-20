package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
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

    @GetMapping("/create")
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

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {
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

    @GetMapping("/delete")
    public String deletePage(@RequestParam("id") Long id, Model model) {
        final Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        final BookDto bookDto = getBookDtoByBook(book);

        model.addAttribute("book", bookDto);
        return "delete";
    }

    @PostMapping("/edit")
    public String editBook(BookDto bookDto) {
        bookService.update(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return "redirect:/list";
    }

    @PostMapping("/delete")
    public String deleteBook(BookDto bookDto) {
        bookService.deleteById(bookDto.getId());
        return "redirect:/list";
    }

    @PostMapping("/create")
    public String createBook(BookDto bookDto) {
        bookService.create(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return "redirect:/list";
    }

    private BookDto getBookDtoByBook(Book book) {
        final BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorId(book.getAuthor().getId());
        bookDto.setAuthorName(book.getAuthor().getFullName());
        bookDto.setGenreId(book.getGenre().getId());
        bookDto.setGenreName(book.getGenre().getName());

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