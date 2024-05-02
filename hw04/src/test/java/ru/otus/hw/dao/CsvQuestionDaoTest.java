package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Question;

import java.util.List;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void checkLoadQuestion() {
        final List<Question> questionList = questionDao.findAll();
        Assertions.assertEquals(3, questionList.size());
    }
}