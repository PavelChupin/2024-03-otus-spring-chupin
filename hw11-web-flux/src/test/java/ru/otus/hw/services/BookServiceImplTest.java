//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import ru.otus.hw.dto.AuthorDto;
//import ru.otus.hw.dto.BookCreateDto;
//import ru.otus.hw.dto.BookDto;
//import ru.otus.hw.dto.BookUpdateDto;
//import ru.otus.hw.dto.GenreDto;
//import ru.otus.hw.models.Author;
//import ru.otus.hw.models.Book;
//import ru.otus.hw.models.Comment;
//import ru.otus.hw.models.Genre;
//import ru.otus.hw.repositories.AuthorRepository;
//import ru.otus.hw.repositories.BookRepository;
//import ru.otus.hw.repositories.CommentRepository;
//import ru.otus.hw.repositories.GenreRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("Сервис по работе с книгами")
//@SpringBootTest
//@Transactional(propagation = Propagation.NEVER)
//class BookServiceImplTest {
//
//    @Autowired
//    private AuthorRepository authorRepository;
//
//    @Autowired
//    private GenreRepository genreRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private BookServiceImpl bookService;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @DisplayName("Должен вернуть книгу по id")
//    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
//    @Test
//    void findByIdTest() {
//        final Book book = bookRepository.findAll().get(0);
//
//        final BookDto actual = bookService.findById(book.getId());
//        final BookDto expected = getBookDtoByBook(book);
//
//        assertThat(actual).isEqualTo(expected);
//
//        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
//        assertThat(actual.getAuthorDto()).isEqualTo(expected.getAuthorDto());
//        assertThat(actual.getAuthorDto().getFullName()).isEqualTo(expected.getAuthorDto().getFullName());
//        assertThat(actual.getGenreDto()).isEqualTo(expected.getGenreDto());
//        assertThat(actual.getGenreDto().getName()).isEqualTo(expected.getGenreDto().getName());
//    }
//
//    @DisplayName("Должен вернуть все книги")
//    @Test
//    void findAllTest() {
//        final List<BookDto> expected = bookRepository.findAll().stream()
//                .map(this::getBookDtoByBook)
//                .collect(Collectors.toList());
//
//        final List<BookDto> actual = bookService.findAll();
//
//        assertThat(actual).isEqualTo(expected);
//        actual.forEach(at ->
//                {
//                    final BookDto bookDto = expected.stream()
//                            .filter(ex -> ex.getId().equals(at.getId()))
//                            .findFirst().get();
//
//                    assertThat(at.getTitle()).isEqualTo(bookDto.getTitle());
//                    assertThat(at.getGenreDto()).isEqualTo(bookDto.getGenreDto());
//                    assertThat(at.getGenreDto().getName()).isEqualTo(bookDto.getGenreDto().getName());
//                    assertThat(at.getAuthorDto()).isEqualTo(bookDto.getAuthorDto());
//                    assertThat(at.getAuthorDto().getFullName()).isEqualTo(bookDto.getAuthorDto().getFullName());
//                }
//        );
//    }
//
//    @DisplayName("Должен обновить книгу")
//    @Test
//    void updateTest() {
//        final BookDto bookDto = getBookDtoByBook(bookRepository.findAll().get(0));
//
//        final String title = "NewTitle";
//        bookDto.setTitle(title);
//
//        final BookUpdateDto bookUpdateDto = getBookUpdateDtoByBook(bookDto);
//        final BookDto actual = bookService.update(bookUpdateDto);
//        final BookDto expected = getBookDtoByBook(bookRepository.findById(bookDto.getId()).get());
//
//        assertThat(actual).isEqualTo(expected);
//        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
//        assertThat(actual.getAuthorDto()).isEqualTo(expected.getAuthorDto());
//        assertThat(actual.getGenreDto()).isEqualTo(expected.getGenreDto());
//    }
//
//    @DisplayName("Должен добавить новую книгу")
//    @Test
//    void createTest() {
//        final Author author = authorRepository.findAll().get(0);
//        final Genre genre = genreRepository.findAll().get(0);
//        final String title = "NewTitle";
//
//        final BookCreateDto bookCreateDto = new BookCreateDto(title, author.getId(), genre.getId());
//        final BookDto actual = bookService.create(bookCreateDto);
//
//        final BookDto expected = getBookDtoByBook(bookRepository.findById(actual.getId()).get());
//
//        assertThat(actual).isEqualTo(expected);
//
//        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
//        assertThat(actual.getAuthorDto()).isEqualTo(expected.getAuthorDto());
//        assertThat(actual.getGenreDto()).isEqualTo(expected.getGenreDto());
//    }
//
//    @DisplayName("Должен удалить книгу по id")
//    @Test
//    void deleteByIdTest() {
//        final List<Book> expected = bookRepository.findAll();
//        final String id = expected.get(0).getId();
//        bookService.deleteById(id);
//        expected.remove(0);
//
//        final List<Book> actual = bookRepository.findAll();
//
//        assertThat(actual).isEqualTo(expected);
//
//        // Проверим что каскадная операция тоже сработала
//        final List<Comment> comments = commentRepository.findAllByBookId(id);
//        assertThat(comments).isEmpty();
//    }
//
//    private BookDto getBookDtoByBook(Book book) {
//        final BookDto bookDto = new BookDto();
//        bookDto.setId(book.getId());
//        bookDto.setTitle(book.getTitle());
//        final AuthorDto authorDto = new AuthorDto();
//        authorDto.setId(book.getAuthor().getId());
//        authorDto.setFullName(book.getAuthor().getFullName());
//        bookDto.setAuthorDto(authorDto);
//        final GenreDto genreDto = new GenreDto();
//        genreDto.setId(book.getGenre().getId());
//        genreDto.setName(book.getGenre().getName());
//        bookDto.setGenreDto(genreDto);
//
//        return bookDto;
//    }
//
//    private BookUpdateDto getBookUpdateDtoByBook(BookDto bookDto) {
//        final BookUpdateDto bookUpdateDto = new BookUpdateDto();
//        bookUpdateDto.setId(bookDto.getId());
//        bookUpdateDto.setTitle(bookDto.getTitle());
//        bookUpdateDto.setGenreId(bookDto.getGenreDto().getId());
//        bookUpdateDto.setAuthorId(bookDto.getAuthorDto().getId());
//        return bookUpdateDto;
//    }
//}