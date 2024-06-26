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
public class AuthorDto {

    @JsonProperty(value = "id", required = true)
    private Long id;

    @JsonProperty(value = "fullName", required = true)
    private String fullName;
}