package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ДТП
 */
@JacksonXmlRootElement
public class Accident {

    /**
     * Название улицы
     */
    @JacksonXmlProperty
    private String street;

    /**
     * Время ДТП
     */
    @JacksonXmlProperty
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timestamp;

    /**
     * Количество участников ДТП
     */
    @JacksonXmlProperty(localName = "vehicles")
    private int vehicles;

    /**
     * ДТП
     * @param street улица
     * @param timestamp время
     * @param vehicles участники
     */
    public Accident(String street, LocalDateTime timestamp, int vehicles) {
        this.street = street;
        this.timestamp = timestamp;
        this.vehicles = vehicles;
    }

    public Accident() {}

    public String getStreet() {
        return street;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getVehicles() {
        return vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accident accident = (Accident) o;
        return vehicles == accident.vehicles &&
                Objects.equals(street, accident.street) &&
                Objects.equals(timestamp, accident.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, timestamp, vehicles);
    }
}
