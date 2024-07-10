package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @DisplayName("Должен искать комментарии для книги")
    @Test
    void findAllByBookIdTest() {
        final String bookId = bookRepository.findAll().blockFirst().getId();
        final Query query = new Query();
        query.addCriteria(Criteria.where("book._id").is(bookId));

        final List<Comment> expected = reactiveMongoTemplate.find(query, Comment.class)
                .collectList()
                .block();

        final Flux<Comment> actual = commentRepository.findAllByBookId(bookId);

        StepVerifier
                .create(actual)
                .assertNext(act -> {
                    final Comment expectedComment = expected.stream()
                            .filter(ex -> ex.getId().equals(act.getId()))
                            .findFirst().get();

                    assertThat(act.getComment()).isEqualTo(expectedComment.getComment());
                })
                .expectComplete()
                .verify();

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
    }

//    @DisplayName("Должен удалить комментарии при удалении книги")
//    @Test
//    void deleteByBookIdAllTest() {
//        final String bookId = bookRepository.findAll().get(0).getId();
//
//        commentRepository.deleteAllByBookId(bookId);
//
//        final Query query = new Query();
//        query.addCriteria(Criteria.where("book._id").is(bookId));
//
//        final Flux<Comment> actual = mongoTemplate.find(query, Comment.class);
//
//        assertThat(actual).isEmpty();
//    }
}