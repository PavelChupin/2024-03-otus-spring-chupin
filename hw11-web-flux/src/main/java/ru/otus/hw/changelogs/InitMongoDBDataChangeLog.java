package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Author author1;

    private Author author2;

    private Author author3;

    private Book book1;

    private Book book2;

    private Book book3;

    @ChangeSet(order = "000", id = "dropDB", author = "chupinpavel", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenres", author = "chupinpavel", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("Genre_1")).block();
        genre2 = repository.save(new Genre("Genre_2")).block();
        genre3 = repository.save(new Genre("Genre_3")).block();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "chupinpavel", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("Author_1")).block();
        author2 = repository.save(new Author("Author_2")).block();
        author3 = repository.save(new Author("Author_3")).block();
    }

    @ChangeSet(order = "003", id = "initBooks", author = "chupinpavel", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = repository.save(new Book("BookTitle_1", author1, genre1)).block();
        book2 = repository.save(new Book("BookTitle_2", author2, genre2)).block();
        book3 = repository.save(new Book("BookTitle_3", author3, genre3)).block();
    }

    @ChangeSet(order = "004", id = "initComments", author = "chupinpavel", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment("comment_1_1", book1)).block();
        repository.save(new Comment("comment_1_2", book1)).block();
        repository.save(new Comment("comment_2_1", book2)).block();
        repository.save(new Comment("comment_2_2", book2)).block();
        repository.save(new Comment("comment_3_1", book3)).block();
        repository.save(new Comment("comment_3_2", book3)).block();
        repository.save(new Comment("comment_3_3", book3)).block();
    }
}
