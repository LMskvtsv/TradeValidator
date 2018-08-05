package ru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.service.validation.checkers.Checkers;


/**
 * Staring point of the application.
 * All checkers initialisation.
 */
@ComponentScan(basePackageClasses = ru.web.ValidationController.class)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Checkers.initCheckers();
        SpringApplication.run(Application.class, args);
    }
}
