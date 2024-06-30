package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookCreateDto {

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