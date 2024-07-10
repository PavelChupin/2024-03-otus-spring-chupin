package ru.otus.hw.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "comment"})
@Document
public class Comment {

    @Id
    private String id;

    private String comment;

    @DBRef(lazy = true)
    private Book book;

    public Comment(String comment, Book book) {
        this.comment = comment;
        this.book = book;
    }
}
