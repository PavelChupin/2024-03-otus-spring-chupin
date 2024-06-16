package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByBookId(String bookId);

    void deleteById(String id);

    Comment updateById(String id, String text);

    Comment addCommentBook(String bookId, String text);
}
