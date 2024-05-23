package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис по работе с комментариями")
@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Должен вернуть все комментарии по id книги")
    @Test
    void findAllByBookId() {
        final Book book = getBook();
        final List<Comment> expected = commentRepository.findAllByBookId(book.getId());
        final List<Comment> actual = commentService.findAllByBookId(book.getId());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен удалить комментарий по id")
    @Test
    void deleteById() {
        final Book book = getBook();
        final List<Comment> expected = commentRepository.findAllByBookId(book.getId());

        commentService.deleteById(expected.get(0).getId());
        expected.remove(0);

        final List<Comment> actual = commentRepository.findAllByBookId(book.getId());
        assertThat(actual).isEqualTo(expected);
    }


    @DisplayName("Должен обновить комментарий по id")
    @Test
    void updateById() {
        final String comment = "NewComment";
        final Book book = getBook();
        final Comment expected = commentRepository.findAllByBookId(book.getId()).get(0);
        expected.setComment(comment);

        final Comment actual = commentService.updateById(expected.getId(), expected.getComment());

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
    }

    @DisplayName("Должен добавить комментарий для книги")
    @Test
    void addCommentBook() {
        final Book book = getBook();
        final List<Comment> expected = commentRepository.findAllByBookId(book.getId());

        final Comment newComment = commentService.addCommentBook(book.getId(), "NewComment");
        expected.add(newComment);

        final List<Comment> actual = commentRepository.findAllByBookId(book.getId());

        assertThat(actual).isEqualTo(expected);
        actual.forEach(at ->
        {
            final Comment comment = expected.stream()
                    .filter(ex -> ex.getId() == at.getId())
                    .findFirst().get();

            assertThat(at.getComment()).isEqualTo(comment.getComment());
        });
    }

    private Book getBook() {
        return bookRepository.findAll().get(0);
    }
}