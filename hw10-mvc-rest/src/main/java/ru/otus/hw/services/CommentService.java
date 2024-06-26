package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByBookId(Long bookId);

    void deleteById(Long id);

    Comment updateById(Long id, String text);

    Comment addCommentBook(Long bookId, String text);
}
