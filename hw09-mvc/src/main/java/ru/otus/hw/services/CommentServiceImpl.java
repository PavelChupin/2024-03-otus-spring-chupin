package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(Long bookId) {
        if (bookId.equals(0L)) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(bookId));
        }

        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id.equals(0L)) {
            throw new EntityNotFoundException("Incorrect comment id %d passed".formatted(id));
        }

        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment updateById(Long id, String text) {
        if (id.equals(0L)) {
            throw new EntityNotFoundException("Incorrect comment id %d passed".formatted(id));
        }

        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setComment(text);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment addCommentBook(Long bookId, String text) {
        if (bookId.equals(0L)) {
            throw new EntityNotFoundException("Incorrect book id %d passed".formatted(bookId));
        }

        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        final Comment comment = new Comment();
        comment.setBook(book);
        comment.setComment(text);
        return commentRepository.save(comment);
    }
}