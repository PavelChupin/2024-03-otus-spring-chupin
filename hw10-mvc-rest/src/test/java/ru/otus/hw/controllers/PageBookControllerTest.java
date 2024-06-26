package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

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

    private static final List<GenreDto> genres = new ArrayList<>();
    private static final List<AuthorDto> authors = new ArrayList<>();
    private static final List<BookDto> books = new ArrayList<>();

    @BeforeAll
    static void init() {
        genres.add(new GenreDto(1L, "Genre1"));
        genres.add(new GenreDto(2L, "Genre2"));
        authors.add(new AuthorDto(1L, "Author1"));
        authors.add(new AuthorDto(2L, "Author2"));
        books.add(new BookDto(1L, "Book1", authors.get(0), genres.get(0)));
        books.add(new BookDto(2L, "Book2", authors.get(1), genres.get(1)));
        books.add(new BookDto(3L, "Book3", authors.get(1), genres.get(1)));
    }

    private void mock() {
        when(bookService.findAll()).thenReturn(books);
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);
        when(bookService.findById(1L)).thenReturn(books.get(0));
    }

    @DisplayName("Должен вернуть страницу списка книг")
    @Test
    void listBooksPageTest() throws Exception {
        mock();
        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", books));
    }


    @DisplayName("Должен вернуть страницу добавления книги")
    @Test
    void createBookPageTest() throws Exception {
        mock();

        mockMvc.perform(get("/create/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("authors", authors));
    }

    @DisplayName("Должен вернуть страницу редактирования книги")
    @Test
    void editPageTest() throws Exception {
        mock();
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/edit/book/%d", bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("book", bookDto));
    }

    @DisplayName("Должен вернуть страницу удаления книги")
    @Test
    void deletePageTest() throws Exception {
        mock();
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/delete/book/%d", bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"))
                .andExpect(model().attribute("book", bookDto));
    }
}