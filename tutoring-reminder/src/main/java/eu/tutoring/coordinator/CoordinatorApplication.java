package eu.tutoring.coordinator;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class CoordinatorApplication {

    public static final Logger log = LoggerFactory.getLogger(CoordinatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CoordinatorApplication.class, args);
    }
}
