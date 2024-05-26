package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByBookId(Long bookId);

    void deleteById(long id);

    Comment updateById(long id, String text);

    Comment addCommentBook(long bookId, String text);
}
