package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов

        questionDao.findAll().forEach(q -> {
            ioService.printFormattedLine(q.getText());
            q.getAnswers().forEach(a -> ioService.printFormattedLine(a.getText()));
        });
    }
}