package deserialization.deserialized_objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Траффик
 */
@JacksonXmlRootElement(localName = "road")
public class Road {

    /**
     * Название улицы
     */
    @JacksonXmlProperty(localName = "street")
    private String street;

    /**
     * Временная метка
     */
    @JacksonXmlProperty(localName = "timestamp")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timestamp;

    /**
     * Уровень траффика
     */
    @JacksonXmlProperty(localName = "trafficLevel")
    private int trafficLevel;

    /**
     * Траффик
     * @param street улица
     * @param timestamp время
     * @param trafficLevel уровень траффика
     */
    public Road(String street, LocalDateTime timestamp, int trafficLevel) {
        this.street = street;
        this.timestamp = timestamp;
        this.trafficLevel = trafficLevel;
    }

    public Road() {}

    public String getStreet() {
        return street;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getTrafficLevel() {
        return trafficLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return trafficLevel == road.trafficLevel &&
                Objects.equals(street, road.street) &&
                Objects.equals(timestamp, road.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, timestamp, trafficLevel);
    }
}
