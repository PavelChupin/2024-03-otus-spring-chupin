package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentService {

    Flux<Comment> findAllByBookId(String bookId);

    void deleteById(String id);

    Mono<Comment> updateById(String id, String text);

    Mono<Comment> addCommentBook(String bookId, String text);
}
