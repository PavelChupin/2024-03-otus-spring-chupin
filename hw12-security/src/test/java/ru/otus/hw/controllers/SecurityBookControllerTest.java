package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
// Обязательно портируем безопастность, т.к. WebMvcTest поднимает усеченный контекст. И он будет тестировать не нашу безопастность, а дефолтную.
@Import(SecurityConfiguration.class)
class SecurityBookControllerTest {

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

    private static UserDetails userDetails;

    @BeforeAll
    static void init() {
        genres.add(new GenreDto(1L, "Genre1"));
        genres.add(new GenreDto(2L, "Genre2"));
        authors.add(new AuthorDto(1L, "Author1"));
        authors.add(new AuthorDto(2L, "Author2"));
        books.add(new BookDto(1L, "Book1", authors.get(0), genres.get(0)));
        books.add(new BookDto(2L, "Book2", authors.get(1), genres.get(1)));
        books.add(new BookDto(3L, "Book3", authors.get(1), genres.get(1)));

        userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username("UnknownUser")
                .password("PavelPass")
                .roles("USER")
                .build();
    }

    @DisplayName("Должен проверить что идет запрос логина и пароля при открытии страницы списка книг")
    @Test
    void listBooksPage() throws Exception {
        mockMvc.perform(get("/list"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что идет запрос логина и пароля при открытии страницы добавления книги")
    @Test
    void createBookPage() throws Exception {
        mockMvc.perform(get("/create/book"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что идет запрос логина и пароля при открытии страницы редактирования книги")
    @Test
    void editPage() throws Exception {
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/edit/book/%d", bookDto.getId())))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что идет запрос логина и пароля при открытии страницы удаления книги")
    @Test
    void deletePage() throws Exception {
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/delete/book/%d", bookDto.getId())))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что запрос изменение книги без авторизации не проходит.")
    @Test
    void update() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookUpdateDto bookUpdateDto = getBookUpdateDtoByBookDto(bookDto);

        mockMvc.perform(post(String.format("/edit/book/%d", bookUpdateDto.getId()))
                .param("id", bookUpdateDto.getId().toString())
                .param("title", bookUpdateDto.getTitle())
                .param("authorId", bookUpdateDto.getAuthorId().toString())
                .param("genreId", bookUpdateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что запрос удаление книги без авторизации не проходит.")
    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(post(String.format("/delete/book/%d", books.get(0).getId())))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Должен проверить что запрос добавления книги без авторизации не проходит.")
    @Test
    void createBook() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookCreateDto bookCreateDto = getBookCreateDtoByBookDto(bookDto);

        mockMvc.perform(post("/create/book")
                .param("title", bookCreateDto.getTitle())
                .param("authorId", bookCreateDto.getAuthorId().toString())
                .param("genreId", bookCreateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection());
    }

    private BookCreateDto getBookCreateDtoByBookDto(BookDto bookdto) {
        final BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setTitle(bookdto.getTitle());
        bookCreateDto.setAuthorId(bookdto.getAuthorDto().getId());
        bookCreateDto.setGenreId(bookdto.getGenreDto().getId());
        return bookCreateDto;
    }

    private BookUpdateDto getBookUpdateDtoByBookDto(BookDto bookDto) {
        final BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(bookDto.getId());
        bookUpdateDto.setTitle(bookDto.getTitle());
        bookUpdateDto.setAuthorId(bookDto.getAuthorDto().getId());
        bookUpdateDto.setGenreId(bookDto.getGenreDto().getId());
        return bookUpdateDto;
    }
}