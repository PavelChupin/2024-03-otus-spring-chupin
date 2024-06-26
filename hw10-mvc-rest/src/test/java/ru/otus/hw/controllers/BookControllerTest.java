package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private static final ObjectMapper jsonMapper = new JsonMapper();

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

    @DisplayName("Должен проверить возврат списка книг")
    @Test
    void listTest() throws Exception {
        final String expected = jsonMapper.writeValueAsString(books);

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/list/api/v1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @DisplayName("Должен проверить изменение книги")
    @Test
    void updateTest() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookUpdateDto bookUpdateDtoByBook = getBookUpdateDtoByBook(bookDto);
        bookUpdateDtoByBook.setTitle("tr");

        final String expected = jsonMapper.writeValueAsString(bookDto);

        when(bookService.update(bookUpdateDtoByBook)).thenReturn(bookDto);

        final String input = jsonMapper.writeValueAsString(bookUpdateDtoByBook);

        mockMvc.perform(put("/edit/book/api/v1").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @DisplayName("Должен проверить валидацию title при изменении книги")
    @Test
    void updateValidTitleTest() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookUpdateDto bookUpdateDtoByBook = getBookUpdateDtoByBook(bookDto);
        bookUpdateDtoByBook.setTitle("tr");

        when(bookService.update(bookUpdateDtoByBook)).thenReturn(bookDto);

        final String input = jsonMapper.writeValueAsString(bookUpdateDtoByBook);

        mockMvc.perform(put("/edit/book/api/v1").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Должен проверить добавление книги")
    @Test
    void createTest() throws Exception {
        final BookDto bookDto = new BookDto(4L, "NewBook", authors.get(0), genres.get(1));

        final BookCreateDto bookCreateDto = new BookCreateDto(bookDto.getTitle(),
                bookDto.getAuthorDto().getId(),
                bookDto.getGenreDto().getId()
        );

        final String input = jsonMapper.writeValueAsString(bookCreateDto);

        when(bookService.create(bookCreateDto)).thenReturn(bookDto);

        mockMvc.perform(post("/create/api/v1").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Должен проверить валидацию title при добавление книги")
    @Test
    void createValidTitleTest() throws Exception {
        final BookCreateDto bookCreateDto = new BookCreateDto("Ne", authors.get(0).getId(), genres.get(1).getId());
        final String input = jsonMapper.writeValueAsString(bookCreateDto);

        mockMvc.perform(post("/create/api/v1").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Должен удалить книгу")
    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete(String.format("/delete/book/api/v1/%d", books.get(2).getId())))
                .andExpect(status().isOk());
    }

    private BookUpdateDto getBookUpdateDtoByBook(BookDto book) {
        final BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(book.getId());
        bookUpdateDto.setTitle(book.getTitle());
        bookUpdateDto.setAuthorId(book.getAuthorDto().getId());
        bookUpdateDto.setGenreId(book.getGenreDto().getId());
        return bookUpdateDto;
    }
}