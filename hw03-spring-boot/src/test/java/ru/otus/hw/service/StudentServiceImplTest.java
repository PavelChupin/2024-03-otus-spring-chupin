package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

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