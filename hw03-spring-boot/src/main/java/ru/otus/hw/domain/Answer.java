package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Answer {
    private final String text;

    private final Boolean isCorrect;
}
