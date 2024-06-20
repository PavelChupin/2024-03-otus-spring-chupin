package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthorDto {
    private Long id;
    private String fullName;
}