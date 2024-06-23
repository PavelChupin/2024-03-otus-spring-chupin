package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class BookUpdateDto {

    private Long id;

    @NotBlank(message = "The book title must contain at least one simbol.")
    @Size(min = 3, message = "The book title cannot be shorter than three characters.")
    private String title;

    private Long authorId;

    private Long genreId;
}