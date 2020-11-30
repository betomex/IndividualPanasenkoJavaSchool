package database.converter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Преобразование объектов java.sql.Date и java.sql.Time в LocalDateTime
 * или извлечения из LocalDateTime даты или времени в формате java.sql.Date и java.sql.Time
 */
public class LocalDateTimeConverter {

    /**
     * Извлечение SQL объекта представления даты из объекта LocalDateTime
     * @param timestamp временная метка
     * @return объект java.sql.Date
     */
    public Date extractDate(LocalDateTime timestamp) {
        if (timestamp != null) {
            return Date.valueOf(timestamp.toLocalDate());
        } else {
            return null;
        }
    }

    /**
     * Извлечение SQL объекта представления времени из объекта LocalDateTime
     * @param timestamp временная метка
     * @return объект java.sql.Time
     */
    public Time extractTime(LocalDateTime timestamp) {
        if (timestamp != null) {
            return Time.valueOf(timestamp.toLocalTime());
        } else {
            return null;
        }
    }

    /**
     * Получение объекта LocalDateTime из SQL объектов представления даты и времени.
     * @param date дата
     * @param time время
     * @return объект LocalDateTime
     */
    public LocalDateTime fromDb(Date date, Time time) {
        if (date != null && time != null) {
            return LocalDateTime.of(sqlDateToLocalDate(date), sqlTimeToLocalTime(time));
        } else {
            return null;
        }
    }

    /**
     * Преобразование SQL объекта представления даты в LocalDate
     * @param date дата
     * @return объект LocalDate
     */
    public LocalDate sqlDateToLocalDate(Date date) {
        if (date != null) {
            return date.toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * Преобразование SQL объекта представления времени в LocalTime.
     * @param time время
     * @return LocalTime
     */
    public LocalTime sqlTimeToLocalTime(Time time) {
        if (time != null) {
            return time.toLocalTime();
        } else {
            return null;
        }
    }
}
