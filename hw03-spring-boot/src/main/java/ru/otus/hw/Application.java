package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        //Создать контекст Spring Boot приложения
        final ApplicationContext context = SpringApplication.run(Application.class, args);
        final var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}