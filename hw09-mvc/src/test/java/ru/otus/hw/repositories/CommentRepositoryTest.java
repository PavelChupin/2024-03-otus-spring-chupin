//package ru.otus.hw.repositories;
//
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import ru.otus.hw.models.Comment;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
//@DataJpaTest
//class CommentRepositoryTest {
//
//    private static final Long BOOK_ID = 1L;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private TestEntityManager em;
//
//    @DisplayName("Должен искать комментарии для книги")
//    @Test
//    void findAllByBookIdTest() {
//        final TypedQuery<Comment> queryComment = em.getEntityManager()
//                .createQuery("select cm from Comment cm where cm.book.id = :id", Comment.class);
//        queryComment.setParameter("id", BOOK_ID);
//
//        final List<Comment> expected = queryComment.getResultList();
//        final List<Comment> actual = commentRepository.findAllByBookId(BOOK_ID);
//
//        assertThat(actual).isEqualTo(expected);
//        actual.forEach(actualComment ->
//                {
//                    final Comment expectedComment = expected.stream()
//                            .filter(ex -> ex.getId().equals(actualComment.getId()))
//                            .findFirst().get();
//
//                    assertThat(actualComment.getComment()).isEqualTo(expectedComment.getComment());
//                }
//        );
//    }
//}