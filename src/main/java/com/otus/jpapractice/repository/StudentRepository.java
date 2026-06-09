package com.otus.jpapractice.repository;

import com.otus.jpapractice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // ============================================================
    // ЗАДАНИЕ: раскомментируй методы по одному и запусти приложение
    // ============================================================

    // 1. Найти всех студентов из конкретного города
    List<Student> findByCity(String city);

    // 2. Найти студента по точному имени (один результат)
    Optional<Student> findByName(String name);

    // 3. Найти всех студентов старше заданного возраста
    List<Student> findByAgeGreaterThan(int age);

    // 4. Найти всех студентов не старше заданного возраста
    List<Student> findByAgeLessThanEqual(int age);

    // 5. Найти студентов в диапазоне возраста
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // 6. Найти студентов, чьё имя содержит подстроку
    List<Student> findByNameContaining(String part);

    // 7. Найти студентов, чьё имя начинается с буквы/слога
    List<Student> findByNameStartingWith(String prefix);

    // 8. Комбинированный: AND — оба условия должны совпасть
    List<Student> findByNameAndCity(String name, String city);

    // 9. Комбинированный: OR — хотя бы одно условие совпадает
    List<Student> findByAgeGreaterThanOrCity(int age, String city);

    // 10. Найти студентов из города, отсортированных по возрасту по убыванию
    List<Student> findByCityOrderByAgeDesc(String city);

    // 11. Найти по email — точное совпадение
    Optional<Student> findByEmail(String email);

    // 12. Найти студентов, email которых заканчивается на домен
    List<Student> findByEmailEndingWith(String suffix);
}
