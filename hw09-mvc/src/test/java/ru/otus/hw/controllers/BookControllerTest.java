package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    private static List<Genre> genres = new ArrayList<>();
    private static List<Author> authors = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();


    @BeforeAll
    static void init() {
        genres.add(new Genre(1L, "Genre1"));
        genres.add(new Genre(2L, "Genre2"));
        authors.add(new Author(1L, "Author1"));
        authors.add(new Author(2L, "Author2"));
        books.add(new Book(1L, "Book1", authors.get(0), genres.get(0)));
        books.add(new Book(2L, "Book2", authors.get(1), genres.get(1)));
        books.add(new Book(3L, "Book3", authors.get(1), genres.get(1)));
    }

    private void mock() {
        when(bookService.findAll()).thenReturn(books);
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);
    }

    @Test
    void listBooksPage() throws Exception {
        mock();
        final List<BookDto> bookDtos = books.stream()
                .map(this::getBookDtoByBook)
                .collect(Collectors.toList());

        mvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @Test
    void createBookPage() throws Exception {
        mock();
        final List<GenreDto> genreDtos = genres.stream()
                .map(this::getGenreDtoByGenre)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authors.stream()
                .map(this::getAuthorDtoByAuthor)
                .collect(Collectors.toList());

        mvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attribute("genres", genreDtos))
                .andExpect(model().attribute("authors", authorDtos));
    }

    @Test
    void editPage() throws Exception {
        mock();
        final BookDto bookDto = getBookDtoByBook(books.get(0));
        final List<GenreDto> genreDtos = genres.stream()
                .map(this::getGenreDtoByGenre)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authors.stream()
                .map(this::getAuthorDtoByAuthor)
                .collect(Collectors.toList());

        mvc.perform(get("/edit").param("id", books.get(0).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("genres", genreDtos))
                .andExpect(model().attribute("authors", authorDtos))
                .andExpect(model().attribute("book", bookDto));
    }

//    @GetMapping("/edit")
//    public String editPage(@RequestParam("id") Long id, Model model) {
//        final Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
//        final BookDto bookDto = getBookDtoByBook(book);
//        final List<GenreDto> genreDtos = genreService.findAll().stream()
//                .map(this::getGenreDtoByGenre)
//                .collect(Collectors.toList());
//        final List<AuthorDto> authorDtos = authorService.findAll().stream()
//                .map(this::getAuthorDtoByAuthor)
//                .collect(Collectors.toList());
//
//        model.addAttribute("book", bookDto);
//        model.addAttribute("genres", genreDtos);
//        model.addAttribute("authors", authorDtos);
//        return "edit";
//    }

    @Test
    void deletePage() {
    }

    //    @GetMapping("/delete")
//    public String deletePage(@RequestParam("id") Long id, Model model) {
//        final Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
//        final BookDto bookDto = getBookDtoByBook(book);
//
//        model.addAttribute("book", bookDto);
//        return "delete";
//    }

    @Test
    void editBook() {
    }


    @Test
    void deleteBook() {
    }

    @Test
    void createBook() {
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