package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
// Обязательно портируем безопастность, т.к. WebMvcTest поднимает усеченный контекст. И он будет тестировать не нашу безопастность, а дефолтную.
@Import(SecurityConfiguration.class)
class BookControllerTest {

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
                .username("Pavel")
                .password("PavelPass")
                .roles("USER")
                .build();
    }

    private void mock() {
        when(bookService.findAll()).thenReturn(books);
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);
        when(bookService.findById(1L)).thenReturn(books.get(0));
    }

    @DisplayName("Должен вернуть страницу списка книг")
    @Test
    void listBooksPage() throws Exception {
        mock();
        mockMvc.perform(get("/list").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", books));
    }


    @DisplayName("Должен вернуть страницу добавления книги")
    @Test
    void createBookPage() throws Exception {
        mock();

        mockMvc.perform(get("/create/book").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("authors", authors));
    }

    @DisplayName("Должен вернуть страницу редактирования книги")
    @Test
    void editPage() throws Exception {
        mock();
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/edit/book/%d", bookDto.getId())).with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("book", bookDto));
    }


    @DisplayName("Должен вернуть страницу удаления книги")
    @Test
    void deletePage() throws Exception {
        mock();
        final BookDto bookDto = books.get(0);

        mockMvc.perform(get(String.format("/delete/book/%d", bookDto.getId())).with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"))
                .andExpect(model().attribute("book", bookDto));
    }

    @DisplayName("Должен изменить книгу")
    @Test
    void update() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookUpdateDto bookUpdateDto = getBookUpdateDtoByBookDto(bookDto);

        when(bookService.update(bookUpdateDto)).thenReturn(bookDto);

        mockMvc.perform(post(String.format("/edit/book/%d", bookUpdateDto.getId())).with(user(userDetails))
                .param("id", bookUpdateDto.getId().toString())
                .param("title", bookUpdateDto.getTitle())
                .param("authorId", bookUpdateDto.getAuthorId().toString())
                .param("genreId", bookUpdateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/list"));
    }

    @DisplayName("Должен вернуть страницу редактирования книги если был передан не допустимый title книги")
    @Test
    void editBookValidTitle() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookUpdateDto bookUpdateDto = getBookUpdateDtoByBookDto(bookDto);
        bookUpdateDto.setTitle("R");

        when(bookService.update(bookUpdateDto)).thenReturn(bookDto);

        mockMvc.perform(post(String.format("/edit/book/%d", bookUpdateDto.getId())).with(user(userDetails))
                .param("id", bookUpdateDto.getId().toString())
                .param("title", bookUpdateDto.getTitle())
                .param("authorId", bookUpdateDto.getAuthorId().toString())
                .param("genreId", bookUpdateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/edit/book/%d", bookUpdateDto.getId())));
    }

    @DisplayName("Должен удалить книгу")
    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(post(String.format("/delete/book/%d", books.get(0).getId())).with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/list"));
    }

    @DisplayName("Должен добавить книгу")
    @Test
    void createBook() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookCreateDto bookCreateDto = getBookCreateDtoByBookDto(bookDto);

        when(bookService.create(bookCreateDto)).thenReturn(bookDto);

        mockMvc.perform(post("/create/book").with(user(userDetails))
                .param("title", bookCreateDto.getTitle())
                .param("authorId", bookCreateDto.getAuthorId().toString())
                .param("genreId", bookCreateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/list"));
    }

    @DisplayName("Должен вернуть на страницу добавления книги")
    @Test
    void createBookValidTitle() throws Exception {
        final BookDto bookDto = books.get(0);
        final BookCreateDto bookCreateDto = getBookCreateDtoByBookDto(bookDto);
        bookCreateDto.setTitle("r");

        when(bookService.create(bookCreateDto)).thenReturn(bookDto);

        mockMvc.perform(post("/create/book").with(user(userDetails))
                .param("title", bookCreateDto.getTitle())
                .param("authorId", bookCreateDto.getAuthorId().toString())
                .param("genreId", bookCreateDto.getGenreId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/create/book"));
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