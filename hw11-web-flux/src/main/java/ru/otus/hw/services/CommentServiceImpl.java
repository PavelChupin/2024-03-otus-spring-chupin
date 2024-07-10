package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<Comment> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Mono<Comment> updateById(String id, String text) {
        final Comment comment = commentRepository.findById(id).toFuture().join();
                //.orElseThrow(() -> new NotFoundException("Comment with id %s not found".formatted(id)));

        comment.setComment(text);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Mono<Comment> addCommentBook(String bookId, String text) {
        final Book book = bookRepository.findById(bookId).toFuture().join();
               // .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));

        final Comment comment = new Comment();
        comment.setBook(book);
        comment.setComment(text);
        return commentRepository.save(comment);
    }
}