package ru.otus.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of= {"id"})
public class BookDto {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private Long genreId;
    private String genreName;
}