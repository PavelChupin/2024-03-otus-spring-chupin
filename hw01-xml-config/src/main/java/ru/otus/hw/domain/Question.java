package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
public class Question {
    private final String text;

    private final List<Answer> answers;
}
