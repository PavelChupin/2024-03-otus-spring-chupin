package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.mockito.Mockito.when;


@SpringBootTest
class StudentServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentService studentService;

    @Test
    void checkException() {
        final Student expected = new Student("Chupin", "Pavel");
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn(expected.getFirstName());
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn(expected.getLastName());

        final Student actual = studentService.determineCurrentStudent();

        Assertions.assertEquals(expected, actual);
    }
}