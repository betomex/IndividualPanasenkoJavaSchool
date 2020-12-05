package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Состояние траффика на дороге, где произошло ДТП, в данный момент времени
 */
public class AccidentRoad {

    private static final String TABLE_NAME = "accident_road";

    /**
     * Айди
     */
    private Integer id;

    /**
     * Улица
     */
    private String street;

    /**
     * Временная метка
     */
    private LocalDateTime timestamp;

    /**
     * Уровень траффика
     */
    private Integer trafficLevel;

    /**
     * Количество участников ДТП
     */
    private Integer vehicles;

    public AccidentRoad(String street, LocalDateTime timestamp, Integer trafficLevel, Integer vehicles) {
        this.street = street;
        this.timestamp = timestamp;
        this.trafficLevel = trafficLevel;
        this.vehicles = vehicles;
    }

    /**
     * Состояние траффика на дороге, где произошло ДТП, в данный момент времени
     * @param id айди
     * @param street улица
     * @param timestamp временная метка
     * @param trafficLevel уровень траффика
     * @param vehicles колво участников ДТП
     */
    public AccidentRoad(Integer id, String street, LocalDateTime timestamp, Integer trafficLevel, Integer vehicles) {
        this.id = id;
        this.street = street;
        this.timestamp = timestamp;
        this.trafficLevel = trafficLevel;
        this.vehicles = vehicles;
    }

    public Integer getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getTrafficLevel() {
        return trafficLevel;
    }

    public Integer getVehicles() {
        return vehicles;
    }

    public void setVehicles(Integer vehicles) {
        this.vehicles = vehicles;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccidentRoad that = (AccidentRoad) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
