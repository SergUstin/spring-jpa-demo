package com.otus.jpapractice.runner;

import com.otus.jpapractice.entity.Student;
import com.otus.jpapractice.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {

    private final StudentRepository repository;

    public DemoRunner(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         SPRING DATA JPA — findBy методы");
        System.out.println("=".repeat(60));

        // --- Все студенты в базе ---
        print("Все студенты в базе", repository.findAll());

        // --- findByCity ---
        print("findByCity(\"Москва\")", repository.findByCity("Москва"));

        // --- findByAgeGreaterThan ---
        print("findByAgeGreaterThan(22)", repository.findByAgeGreaterThan(22));

        // --- findByAgeLessThanEqual ---
        print("findByAgeLessThanEqual(20)", repository.findByAgeLessThanEqual(20));

        // --- findByAgeBetween ---
        print("findByAgeBetween(21, 23)", repository.findByAgeBetween(21, 23));

        // --- findByNameContaining ---
        print("findByNameContaining(\"ан\")", repository.findByNameContaining("ан"));

        // --- findByNameStartingWith ---
        print("findByNameStartingWith(\"А\")", repository.findByNameStartingWith("А"));

        // --- findByNameAndCity ---
        print("findByNameAndCity(\"Анна\", \"Москва\")",
                repository.findByNameAndCity("Анна", "Москва"));

        // --- findByAgeGreaterThanOrCity ---
        print("findByAgeGreaterThanOrCity(24, \"Казань\")",
                repository.findByAgeGreaterThanOrCity(24, "Казань"));

        // --- findByCityOrderByAgeDesc ---
        print("findByCityOrderByAgeDesc(\"Санкт-Петербург\")",
                repository.findByCityOrderByAgeDesc("Санкт-Петербург"));

        // --- findByEmailEndingWith ---
        print("findByEmailEndingWith(\"@gmail.com\")",
                repository.findByEmailEndingWith("@gmail.com"));

        // --- findByName (Optional) ---
        System.out.println("\n>> findByName(\"Иван\")");
        repository.findByName("Иван")
                .ifPresentOrElse(
                        s -> System.out.println("   Найден: " + s),
                        () -> System.out.println("   Не найден"));

        // --- findByEmail (Optional) ---
        System.out.println("\n>> findByEmail(\"alex@gmail.com\")");
        repository.findByEmail("alex@gmail.com")
                .ifPresentOrElse(
                        s -> System.out.println("   Найден: " + s),
                        () -> System.out.println("   Не найден"));

        System.out.println("\n" + "=".repeat(60));
        System.out.println(" ПОПРОБУЙ САМ: измени аргументы вызовов выше и перезапусти!");
        System.out.println(" H2 консоль: http://localhost:8080/h2-console");
        System.out.println(" JDBC URL:   jdbc:h2:mem:practicedb  (user: sa, pass: пусто)");
        System.out.println("=".repeat(60) + "\n");
    }

    private void print(String label, List<Student> students) {
        System.out.println("\n>> " + label);
        if (students.isEmpty()) {
            System.out.println("   (пусто)");
        } else {
            students.forEach(s -> System.out.println("   " + s));
        }
    }
}
