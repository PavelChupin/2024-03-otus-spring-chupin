package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {
    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    void executeTestFor() {
        final Student student = new Student("Chupin", "Pavel");
        final List<Question> questionList = new ArrayList<>();
        questionList.add(new Question("question1"
                , Arrays.asList(new Answer("answer1", true), new Answer("answer2", false)))
        );
        questionList.add(new Question("question2"
                , Arrays.asList(new Answer("answer1", true), new Answer("answer2", false)))
        );

        final TestResult expected = new TestResult(student);
        questionList.forEach(q -> expected.applyAnswer(q, true));

        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readStringWithPrompt("Select an option.")).thenReturn("1");

        final TestResult actual = testService.executeTestFor(student);

        assertEquals(expected, actual);
    }
}