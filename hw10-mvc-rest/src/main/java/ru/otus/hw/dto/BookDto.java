package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @JsonProperty(value = "id", required = true)
    private Long id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "author", required = true)
    private AuthorDto authorDto;

    @JsonProperty(value = "genre", required = true)
    private GenreDto genreDto;
}