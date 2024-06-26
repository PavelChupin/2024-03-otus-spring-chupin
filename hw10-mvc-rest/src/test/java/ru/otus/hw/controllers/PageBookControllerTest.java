package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PageBookController.class)
class PageBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    private static final List<Genre> genres = new ArrayList<>();
    private static final List<Author> authors = new ArrayList<>();
    private static final List<Book> books = new ArrayList<>();

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
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
    }

    @DisplayName("Должен вернуть страницу списка книг")
    @Test
    void listBooksPage() throws Exception {
        mock();
        final List<BookDto> bookDtos = books.stream()
                .map(this::getBookDtoByBook)
                .collect(Collectors.toList());

        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", bookDtos));
    }


    @DisplayName("Должен вернуть страницу добавления книги")
    @Test
    void createBookPage() throws Exception {
        mock();
        final List<GenreDto> genreDtos = genres.stream()
                .map(this::getGenreDtoByGenre)
                .collect(Collectors.toList());
        final List<AuthorDto> authorDtos = authors.stream()
                .map(this::getAuthorDtoByAuthor)
                .collect(Collectors.toList());

        mockMvc.perform(get("/create/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attribute("genres", genreDtos))
                .andExpect(model().attribute("authors", authorDtos));
    }

    @DisplayName("Должен вернуть страницу редактирования книги")
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

        mockMvc.perform(get(String.format("/edit/book/%d", bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("genres", genreDtos))
                .andExpect(model().attribute("authors", authorDtos))
                .andExpect(model().attribute("book", bookDto));
    }


    @DisplayName("Должен вернуть страницу удаления книги")
    @Test
    void deletePage() throws Exception {
        mock();
        final BookDto bookDto = getBookDtoByBook(books.get(0));

        mockMvc.perform(get(String.format("/delete/book/%d", bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"))
                .andExpect(model().attribute("book", bookDto));
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