package ru.otus.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class GenreDto {
    private Long id;
    private String name;
}