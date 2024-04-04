package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.PrintStream;

class TestServiceImplTest {

    @Test
    void checkException() {
        final AppProperties testFileNameProvider = new AppProperties("questions.csv");
        final QuestionDao questionDao = new CsvQuestionDao(testFileNameProvider);
        final IOService ioService = new StreamsIOService(new PrintStream(java.lang.System.out));
        final TestService testService = new TestServiceImpl(ioService, questionDao);

        Assertions.assertDoesNotThrow(testService::executeTest);

        testFileNameProvider.setTestFileName("questions.txt");

        Assertions.assertThrows(QuestionReadException.class, testService::executeTest);
    }
}