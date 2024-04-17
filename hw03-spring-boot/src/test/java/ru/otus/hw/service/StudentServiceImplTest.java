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
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void checkException() {
        final Student expected = new Student("Chupin", "Pavel");
        given(ioService.readStringWithPrompt("Please input your first name"))
                .willReturn(expected.getFirstName());
        given(ioService.readStringWithPrompt("Please input your last name"))
                .willReturn(expected.getLastName());

        final Student actual = studentService.determineCurrentStudent();

        Assertions.assertEquals(expected, actual);
    }
}