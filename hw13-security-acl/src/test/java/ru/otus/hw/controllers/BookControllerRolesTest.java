package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.mapper.AuthorMapper;
import ru.otus.hw.services.mapper.BookMapper;
import ru.otus.hw.services.mapper.GenreMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
// Обязательно портируем безопастность, т.к. WebMvcTest поднимает усеченный контекст. И он будет тестировать не нашу безопастность, а дефолтную.
@Import({SecurityConfiguration.class})
class BookControllerRolesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    private static final String USER_NAME = "user";

    private static final List<Genre> genres = new ArrayList<>();
    private static final List<Author> authors = new ArrayList<>();
    private static final List<Book> books = new ArrayList<>();
    private static final List<GenreDto> genreDtos = new ArrayList<>();
    private static final List<AuthorDto> authorDtos = new ArrayList<>();
    private static final List<BookDto> bookDtos = new ArrayList<>();

    @BeforeAll
    static void init() {
        genres.add(new Genre(1L, "Genre1"));
        genres.add(new Genre(2L, "Genre2"));
        authors.add(new Author(1L, "Author1"));
        authors.add(new Author(2L, "Author2"));
        books.add(new Book(1L, "Book1", authors.get(0), genres.get(0)));
        books.add(new Book(2L, "Book2", authors.get(1), genres.get(1)));
        books.add(new Book(3L, "Book3", authors.get(1), genres.get(1)));
        authorDtos.addAll(authors.stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList()));
        genreDtos.addAll(genres.stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList()));
        bookDtos.addAll(books.stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList()));
    }

    private void mock() {
        when(bookService.findAll()).thenReturn(books);
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);
        when(bookService.findById(1L)).thenReturn(books.get(0));
    }

    @DisplayName("Должен вернуть страницу списка книг для всех пользователей")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void listBooksPage(UserDetails userDetails) throws Exception {
        mock();

        mockMvc.perform(get("/list").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @DisplayName("Должен проверить и вернуть страницу добавления книги, только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void createBookPage(UserDetails userDetails) throws Exception {
        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(get("/create/book").with(user(userDetails)))
                    .andExpect(status().isForbidden());
        } else {
            mock();
                    mockMvc.perform(get("/create/book").with(user(userDetails)))
                            .andExpect(status().isOk())
                            .andExpect(view().name("create"))
                            .andExpect(model().attribute("genres", genreDtos))
                            .andExpect(model().attribute("authors", authorDtos));
        }
    }

    @DisplayName("Должен вернуть страницу редактирования книги, только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void editPage(UserDetails userDetails) throws Exception {
        final Book book = books.get(0);

        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(get(String.format("/edit/book/%d", book.getId())).with(user(userDetails)))
                    .andExpect(status().isForbidden());
        } else {
            mock();
            BookDto bookDto = BookMapper.toDto(book);
            mockMvc.perform(get(String.format("/edit/book/%d", book.getId())).with(user(userDetails)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("edit"))
                    .andExpect(model().attribute("genres", genreDtos))
                    .andExpect(model().attribute("authors", authorDtos))
                    .andExpect(model().attribute("book", bookDto));
        }
    }

    @DisplayName("Должен вернуть страницу удаления книги, только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void deletePage(UserDetails userDetails) throws Exception {
        final Book book = books.get(0);
        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(get(String.format("/delete/book/%d", book.getId())).with(user(userDetails)))
                    .andExpect(status().isForbidden());
        } else {
            mock();
            BookDto bookDto = BookMapper.toDto(book);
            mockMvc.perform(get(String.format("/delete/book/%d", book.getId())).with(user(userDetails)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("delete"))
                    .andExpect(model().attribute("book", bookDto));
        }
    }

    @DisplayName("Должен изменить книгу, доступно только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void update(UserDetails userDetails) throws Exception {
        final Book book = books.get(0);
        final BookUpdateDto bookUpdateDto = getBookUpdateDtoByBookDto(book);

        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(post(String.format("/edit/book/%d", bookUpdateDto.getId())).with(user(userDetails))
                    .param("id", bookUpdateDto.getId().toString())
                    .param("title", bookUpdateDto.getTitle())
                    .param("authorId", bookUpdateDto.getAuthorId().toString())
                    .param("genreId", bookUpdateDto.getGenreId().toString())
            )
                    .andExpect(status().isForbidden());
        } else {
            when(bookService.update(book, bookUpdateDto.getTitle(), bookUpdateDto.getAuthorId(), bookUpdateDto.getGenreId()))
                    .thenReturn(book);
            mockMvc.perform(post(String.format("/edit/book/%d", bookUpdateDto.getId())).with(user(userDetails))
                    .param("id", bookUpdateDto.getId().toString())
                    .param("title", bookUpdateDto.getTitle())
                    .param("authorId", bookUpdateDto.getAuthorId().toString())
                    .param("genreId", bookUpdateDto.getGenreId().toString())
            )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/list"));
        }
    }

    @DisplayName("Должен удалить книгу, доступно только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void deleteBook(UserDetails userDetails) throws Exception {
        final Long bookId = books.get(0).getId();
        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(post(String.format("/delete/book/%d", bookId)).with(user(userDetails)))
                    .andExpect(status().isForbidden());
        } else {
            mockMvc.perform(post(String.format("/delete/book/%d", bookId)).with(user(userDetails)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/list"));
        }
    }

    @DisplayName("Должен добавить книгу, доступно только для ADMIN и LIB")
    @ParameterizedTest
    @MethodSource("userDetailsParams")
    void createBook(UserDetails userDetails) throws Exception {
        final Book book = books.get(0);
        final BookCreateDto bookCreateDto = getBookCreateDtoByBookDto(book);

        if (USER_NAME.equals(userDetails.getUsername())) {
            mockMvc.perform(post("/create/book").with(user(userDetails))
                    .param("title", bookCreateDto.getTitle())
                    .param("authorId", bookCreateDto.getAuthorId().toString())
                    .param("genreId", bookCreateDto.getGenreId().toString())
            )
                    .andExpect(status().isForbidden());
        } else {
            when(bookService.create(bookCreateDto)).thenReturn(book);

            mockMvc.perform(post("/create/book").with(user(userDetails))
                    .param("title", bookCreateDto.getTitle())
                    .param("authorId", bookCreateDto.getAuthorId().toString())
                    .param("genreId", bookCreateDto.getGenreId().toString())
            )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/list"));
        }
    }


    static Stream<UserDetails> userDetailsParams() {
        UserDetails admin = org.springframework.security.core.userdetails.User
                .builder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        UserDetails user = org.springframework.security.core.userdetails.User
                .builder()
                .username(USER_NAME)
                .password("user")
                .roles("USER")
                .build();

        UserDetails lib = org.springframework.security.core.userdetails.User
                .builder()
                .username("lib")
                .password("lib")
                .roles("LIBRARIAN")
                .build();

        return Stream.of(admin, user, lib);
    }

    private BookCreateDto getBookCreateDtoByBookDto(Book book) {
        final BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setTitle(book.getTitle());
        bookCreateDto.setAuthorId(book.getAuthor().getId());
        bookCreateDto.setGenreId(book.getGenre().getId());
        return bookCreateDto;
    }

    private BookUpdateDto getBookUpdateDtoByBookDto(Book book) {
        final BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(book.getId());
        bookUpdateDto.setTitle(book.getTitle());
        bookUpdateDto.setAuthorId(book.getAuthor().getId());
        bookUpdateDto.setGenreId(book.getGenre().getId());
        return bookUpdateDto;
    }
}