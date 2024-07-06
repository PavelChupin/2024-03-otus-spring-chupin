package ru.otus.hw.services.mapper;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

public class AuthorMapper {

    public static AuthorDto toDto(Author author) {
        final AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFullName(author.getFullName());

        return authorDto;
    }
}