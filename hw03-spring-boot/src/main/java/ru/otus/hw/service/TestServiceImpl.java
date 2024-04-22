package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printQuestions(question);
            final var numberAnswer = ioService.readIntForRangeWithPromptLocalized(1, question.getAnswers().size()
            ,"Select.option", "Incorrect.selected.option");
            final var isAnswerValid = question.getAnswers().get(numberAnswer - 1).getIsCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }

        return testResult;
    }

    private void printQuestions(Question question) {
        ioService.printFormattedLine(question.getText());
        final AtomicInteger number = new AtomicInteger(1);
        question.getAnswers().forEach(a -> ioService.printFormattedLine(number.getAndIncrement() + ". " + a.getText()));
    }
}