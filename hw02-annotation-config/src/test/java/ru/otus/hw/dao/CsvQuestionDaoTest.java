package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

public class CsvQuestionDaoTest {
    @Test
    public void checkLoadQuestion(){
        final TestFileNameProvider testFileNameProvider = new AppProperties(1,"questions_test.csv");
        final CsvQuestionDao csvQuestionDao = new CsvQuestionDao(testFileNameProvider);

        final List<Question> questionList = csvQuestionDao.findAll();
        Assertions.assertEquals(3, questionList.size());
    }
}