package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями")
@DataMongoTest
class CommentRepositoryTest {

    private static final Long BOOK_ID = 1L;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Должен искать комментарии для книги")
    @Test
    void findAllByBookIdTest() {
        final String bookId = bookRepository.findAll().get(0).getId();
        final Query query = new Query();
        query.addCriteria(Criteria.where("book._id").is(bookId));

        final List<Comment> expected = mongoTemplate.find(query, Comment.class);
        final List<Comment> actual = commentRepository.findAllByBookId(bookId);

        assertThat(actual).isEqualTo(expected);
        actual.forEach(actualComment ->
                {
                    final Comment expectedComment = expected.stream()
                            .filter(ex -> ex.getId().equals(actualComment.getId()))
                            .findFirst().get();

                    assertThat(actualComment.getComment()).isEqualTo(expectedComment.getComment());
                }
        );
    }
}