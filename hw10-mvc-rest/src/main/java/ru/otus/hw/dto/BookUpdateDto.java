package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto {

    @JsonProperty(value = "id", required = true)
    @NotNull
    private Long id;

    @NotBlank(message = "The book title must contain at least one simbol.")
    @Length(min = 3, message = "The book title cannot be shorter than three characters.")
    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "authorId", required = true)
    @NotNull
    private Long authorId;

    @JsonProperty(value = "genreId", required = true)
    @NotNull
    private Long genreId;
}