package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTests {
    @Autowired
    private FilmController filmController;

    @Test
    public void ifFilmNameIsBlankThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("");
        film.setReleaseDate(LocalDate.of(1994, 1, 1));
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.addFilm(film));

    }

    @Test
    public void ifFilmDescriptionLengthIsMoreThen200ThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("300 Спартанцев");
        film.setReleaseDate(LocalDate.of(2012, 1, 1));
        film.setDescription("300 Спартанцев".repeat(300));
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void ifFilmReleaseDateIsBefore1895_12_28ThenThrowException() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("Побег из Шоушенка");
        film.setReleaseDate(LocalDate.of(1794, 1, 1));
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Дата релиза фильма не может быть раньше 1895-12-28", exception.getMessage());
    }

    @Test
    public void ifFilmDurationIsNegativeThenThrowException() {
        Film film = new Film();
        film.setName("Побег из Шоушенка");
        film.setDuration(-1);
        film.setReleaseDate(LocalDate.of(1994, 1, 1));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.addFilm(film));
    }

}