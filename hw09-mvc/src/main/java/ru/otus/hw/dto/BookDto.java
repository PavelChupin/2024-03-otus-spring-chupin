package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookDto {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private Long genreId;
    private String genreName;
}