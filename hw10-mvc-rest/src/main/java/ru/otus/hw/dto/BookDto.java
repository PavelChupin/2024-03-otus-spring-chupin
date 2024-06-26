package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class BookDto {

    private Long id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "author", required = true)
    private AuthorDto authorDto;

    @JsonProperty(value = "genre", required = true)
    private GenreDto genreDto;
}