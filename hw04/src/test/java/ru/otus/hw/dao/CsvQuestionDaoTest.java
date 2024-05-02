package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvQuestionDaoTest {

    @Test
    public void checkLoadQuestion() {
        final AppProperties appProperties = new AppProperties();
        appProperties.setRightAnswersCountToPass(1);
        appProperties.setLocale("ru-RU");
        final Map<String, String> file = new HashMap<>();
        file.put("ru-RU", "questions_test.csv");
        appProperties.setFileNameByLocaleTag(file);

        final CsvQuestionDao csvQuestionDao = new CsvQuestionDao(appProperties);

        final List<Question> questionList = csvQuestionDao.findAll();
        Assertions.assertEquals(3, questionList.size());
    }
}