package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void checkException() {
        final Student student = new Student("Chupin", "Pavel");
        given(ioService.readStringWithPrompt("Please input your first name"))
                .willReturn(student.getFirstName());
        given(ioService.readStringWithPrompt("Please input your last name"))
                .willReturn(student.getLastName());

        final Student actual = studentService.determineCurrentStudent();

        Assertions.assertEquals(student, actual);
    }
}