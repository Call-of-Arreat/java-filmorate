package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Получить всех юзеров");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validateUser(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getId() != null) {
            if (users.containsKey(user.getId())) {
                validateUser(user);
                users.put(user.getId(), user);
                log.info("Пользователь обновлен: {}", user);
                return user;
            }
        }
        log.warn("User id is invalid");
        throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Неверный адрес электронной почты: ", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Неверный логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неверный день рождения: ", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
