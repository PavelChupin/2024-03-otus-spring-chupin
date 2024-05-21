package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import(CommentRepositoryImpl.class)
class CommentRepositoryImplTest {

    private static final long COMMENT_ID = 1L;

    @Autowired
    private CommentRepositoryImpl commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Должен вернуть комментарий по id")
    @Test
    void findById() {
        final Comment expected = em.find(Comment.class, COMMENT_ID);
        final Optional<Comment> actual = commentRepository.findById(COMMENT_ID);

        assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @DisplayName("Должен сохранять комментарий")
    @Test
    void save() {
        final Book book = getBooks().get(0);

        final TypedQuery<Comment> queryComment = em.getEntityManager()
                .createQuery("select cm from Comment cm", Comment.class);

        final List<Comment> expected = queryComment.getResultList();

        final Comment newComment = new Comment();
        newComment.setComment("NewComment");
        newComment.setBook(book);

        expected.add(newComment);
        commentRepository.save(newComment);
        final List<Comment> actual = queryComment.getResultList();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    void deleteById() {
        final TypedQuery<Comment> queryComment = em.getEntityManager()
                .createQuery("select cm from Comment cm", Comment.class);

        final List<Comment> expected = queryComment.getResultList();

        commentRepository.deleteById(expected.get(0).getId());
        expected.remove(0);

        final List<Comment> actual = queryComment.getResultList();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Должен искать комментарии для книги")
    @Test
    void findAllByBookId() {
        final Book book = getBooks().get(0);

        final TypedQuery<Comment> queryComment = em.getEntityManager()
                .createQuery("select cm from Comment cm where cm.book.id = :id", Comment.class);
        queryComment.setParameter("id", book.getId());

        final List<Comment> expected = queryComment.getResultList();

        final List<Comment> actual = commentRepository.findAllByBookId(book.getId());

        assertThat(actual).isEqualTo(expected);
    }

    private List<Book> getBooks() {
        final TypedQuery<Book> queryBook = em.getEntityManager()
                .createQuery("select bk from Book bk", Book.class);

        return queryBook.getResultList();
    }
}