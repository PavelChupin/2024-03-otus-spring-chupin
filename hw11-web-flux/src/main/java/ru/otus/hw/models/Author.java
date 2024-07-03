package ru.otus.hw.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "fullName"})
@Getter
@Setter
@Document
public class Author {

    @Id
    private String id;

    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }
}