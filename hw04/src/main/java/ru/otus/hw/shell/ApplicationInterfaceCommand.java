package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Application interface command.")
@RequiredArgsConstructor
public class ApplicationInterfaceCommand {

    private Student student;

    private TestResult testResult;

    private final TestRunnerService testRunnerService;

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @ShellMethod(value = "Run application test of students for automate.", key = {"automation", "auto"})
    public String runAutomationTest() {
        testRunnerService.run();
        return "Test service is running !!!!";
    }

    @ShellMethod(value = "Insert student name.", key = {"name", "n", "est"})
    public String enterStudentName() {
        this.student = studentService.determineCurrentStudent();
        return "Enter student name is completed.";
    }

    @ShellMethod(value = "Run test for student.", key = {"run", "rn"})
    @ShellMethodAvailability(value = "isStudentName")
    private String runTest() {
        this.testResult = testService.executeTestFor(this.student);
        return String.format("Test for student %s is completed.", this.student.getFullName()) ;
    }

    private Availability isStudentName() {
        return this.student == null
                ? Availability.unavailable("Please, enter student name.")
                : Availability.available();
    }

    @ShellMethod(value = "Print result test for student.", key = {"pr", "print"})
    @ShellMethodAvailability(value = "isPrintResultTest")
    private void printResultTest() {
        resultService.showResult(testResult);
    }

    private Availability isPrintResultTest() {
        return testResult == null
                ? Availability.unavailable("Testing has not yet been completed.")
                : Availability.available();
    }
}