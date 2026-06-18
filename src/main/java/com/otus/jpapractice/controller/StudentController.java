package com.otus.jpapractice.controller;

import com.otus.jpapractice.entity.Student;
import com.otus.jpapractice.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    public record ParamInfo(String name, String label, String placeholder, String type) {}

    public record MethodInfo(
        String id,
        String signature,
        String jpql,
        String comment,
        List<ParamInfo> params
    ) {}

    public record SearchResult(
        String method,
        String signature,
        String jpql,
        int count,
        List<Student> results
    ) {}

    private static final List<MethodInfo> METHODS = List.of(
        new MethodInfo("findByCity",
            "List<Student> findByCity(String city)",
            "SELECT s FROM Student s\nWHERE s.city = :city",
            "Найти всех студентов из города",
            List.of(new ParamInfo("p1", "city", "Москва", "text"))),

        new MethodInfo("findByName",
            "Optional<Student> findByName(String name)",
            "SELECT s FROM Student s\nWHERE s.name = :name",
            "Найти студента по имени (Optional)",
            List.of(new ParamInfo("p1", "name", "Иван", "text"))),

        new MethodInfo("findByAgeGreaterThan",
            "List<Student> findByAgeGreaterThan(int age)",
            "SELECT s FROM Student s\nWHERE s.age > :age",
            "Найти студентов старше N лет",
            List.of(new ParamInfo("p1", "age", "22", "number"))),

        new MethodInfo("findByAgeLessThanEqual",
            "List<Student> findByAgeLessThanEqual(int age)",
            "SELECT s FROM Student s\nWHERE s.age <= :age",
            "Найти студентов не старше N лет",
            List.of(new ParamInfo("p1", "age", "20", "number"))),

        new MethodInfo("findByAgeBetween",
            "List<Student> findByAgeBetween(int minAge, int maxAge)",
            "SELECT s FROM Student s\nWHERE s.age BETWEEN :minAge AND :maxAge",
            "Диапазон возраста (BETWEEN)",
            List.of(
                new ParamInfo("p1", "minAge", "21", "number"),
                new ParamInfo("p2", "maxAge", "23", "number")
            )),

        new MethodInfo("findByNameContaining",
            "List<Student> findByNameContaining(String part)",
            "SELECT s FROM Student s\nWHERE s.name LIKE '%' || :part || '%'",
            "Имя содержит подстроку (LIKE %...%)",
            List.of(new ParamInfo("p1", "part", "ан", "text"))),

        new MethodInfo("findByNameStartingWith",
            "List<Student> findByNameStartingWith(String prefix)",
            "SELECT s FROM Student s\nWHERE s.name LIKE :prefix || '%'",
            "Имя начинается с... (LIKE ...%)",
            List.of(new ParamInfo("p1", "prefix", "А", "text"))),

        new MethodInfo("findByNameAndCity",
            "List<Student> findByNameAndCity(String name, String city)",
            "SELECT s FROM Student s\nWHERE s.name = :name\n  AND s.city = :city",
            "По имени И городу (AND)",
            List.of(
                new ParamInfo("p1", "name", "Анна", "text"),
                new ParamInfo("p2", "city", "Москва", "text")
            )),

        new MethodInfo("findByAgeGreaterThanOrCity",
            "List<Student> findByAgeGreaterThanOrCity(int age, String city)",
            "SELECT s FROM Student s\nWHERE s.age > :age\n   OR s.city = :city",
            "По возрасту ИЛИ городу (OR)",
            List.of(
                new ParamInfo("p1", "age", "24", "number"),
                new ParamInfo("p2", "city", "Казань", "text")
            )),

        new MethodInfo("findByCityOrderByAgeDesc",
            "List<Student> findByCityOrderByAgeDesc(String city)",
            "SELECT s FROM Student s\nWHERE s.city = :city\nORDER BY s.age DESC",
            "По городу, сортировка по возрасту (ORDER BY)",
            List.of(new ParamInfo("p1", "city", "Санкт-Петербург", "text"))),

        new MethodInfo("findByEmail",
            "Optional<Student> findByEmail(String email)",
            "SELECT s FROM Student s\nWHERE s.email = :email",
            "Найти по email (Optional)",
            List.of(new ParamInfo("p1", "email", "alex@gmail.com", "text"))),

        new MethodInfo("findByEmailEndingWith",
            "List<Student> findByEmailEndingWith(String suffix)",
            "SELECT s FROM Student s\nWHERE s.email LIKE '%' || :suffix",
            "Email заканчивается на... (LIKE %...)",
            List.of(new ParamInfo("p1", "suffix", "@gmail.com", "text")))
    );

    @GetMapping("/methods")
    public List<MethodInfo> getMethods() {
        return METHODS;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/search")
    public SearchResult search(
            @RequestParam String method,
            @RequestParam(required = false) String p1,
            @RequestParam(required = false) String p2) {

        MethodInfo info = METHODS.stream()
                .filter(m -> m.id().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown method: " + method));

        List<Student> results = switch (method) {
            case "findByCity"              -> repository.findByCity(p1);
            case "findByName"             -> toList(repository.findByName(p1));
            case "findByAgeGreaterThan"   -> repository.findByAgeGreaterThan(Integer.parseInt(p1));
            case "findByAgeLessThanEqual" -> repository.findByAgeLessThanEqual(Integer.parseInt(p1));
            case "findByAgeBetween"       -> repository.findByAgeBetween(Integer.parseInt(p1), Integer.parseInt(p2));
            case "findByNameContaining"   -> repository.findByNameContaining(p1);
            case "findByNameStartingWith" -> repository.findByNameStartingWith(p1);
            case "findByNameAndCity"      -> repository.findByNameAndCity(p1, p2);
            case "findByAgeGreaterThanOrCity" -> repository.findByAgeGreaterThanOrCity(Integer.parseInt(p1), p2);
            case "findByCityOrderByAgeDesc"   -> repository.findByCityOrderByAgeDesc(p1);
            case "findByEmail"            -> toList(repository.findByEmail(p1));
            case "findByEmailEndingWith"  -> repository.findByEmailEndingWith(p1);
            default -> throw new IllegalArgumentException("Unknown method: " + method);
        };

        return new SearchResult(info.id(), info.signature(), info.jpql(), results.size(), results);
    }

    private List<Student> toList(Optional<Student> opt) {
        return opt.map(List::of).orElse(List.of());
    }
}
