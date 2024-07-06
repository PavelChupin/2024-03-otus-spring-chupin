package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    @JsonProperty(value = "id", required = true)
    private Long id;

    @JsonProperty(value = "fullName", required = true)
    private String fullName;
}