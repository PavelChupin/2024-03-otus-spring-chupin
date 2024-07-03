package ru.otus.hw.events;


import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCascadeDeleteEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        final String bookId = event.getSource().get("_id").toString();

        final List<Comment> comments = commentRepository.findAllByBookId(bookId);
        commentRepository.deleteAll(comments);
    }
}
