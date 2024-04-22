package ru.otus.hw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Student {
    private final String firstName;

    private final String lastName;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}