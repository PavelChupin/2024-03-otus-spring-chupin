package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommnetCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comment by book id.", key = "cmbk")
    public String findAllComments(long bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Add comment by book id.", key = "cmad")
    public String addCommentBook(long bookId, String text) {
        final Comment comment = commentService.addCommentBook(bookId, text);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Delete comment by id.", key = "cmdl")
    public String deleteComment(long id) {
        commentService.deleteById(id);
        return "Delete comment is completed.";
    }

    @ShellMethod(value = "Update comment by id.", key = "cmupd")
    public String updateComment(long id, String text) {
        final Comment comment = commentService.updateById(id, text);
        return commentConverter.commentToString(comment);
    }
}
