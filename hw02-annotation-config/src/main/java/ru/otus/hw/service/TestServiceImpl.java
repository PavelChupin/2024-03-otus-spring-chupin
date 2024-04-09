package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        final var questions = questionDao.findAll();
        final var testResult = new TestResult(student);

        for (final var question : questions) {
            printQuestions(question);
            final var numberAnswer = Integer.parseInt(ioService.readStringWithPrompt("Select an option."));
            final var isAnswerValid = question.getAnswers().get(numberAnswer - 1).getIsCorrect(); // Задать вопрос, получить ответ
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
