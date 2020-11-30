package combine;

import database.model.AccidentRoad;
import deserialization.deserialized_objects.Accident;
import deserialization.deserialized_objects.Road;

import java.util.ArrayList;
import java.util.List;

/**
 * Поиск списка состояний траффика на дороге в момент ДТП
 */
public class RoadAccidentCombiner {

    /**
     * Счётчик объектов
     */
    private static int idCounter = 1;

    /**
     * Список улиц с траффиком
     */
    private List<Road> roads;

    /**
     * Список ДТП
     */
    private List<Accident> accidents;

    /**
     * Результирующий список с состояниями траффика на дороге, где произошло ДТП, на момент времени
     */
    private List<AccidentRoad> targetList;

    /**
     * Поиск состояний траффика...
     * @param roads дороги
     * @param accidents дтп
     */
    public RoadAccidentCombiner(List<Road> roads, List<Accident> accidents) {
        this.roads = roads;
        this.accidents = accidents;
        targetList = new ArrayList<>();
    }

    /**
     * Поиск совпадений между Road и Accident
     * @return результирующий список
     */
    public List<AccidentRoad> combine() {
        for (Road road: roads) {
            for (Accident accident: accidents) {
                if (checkStreetAndTimeStamp(road, accident)) {
                    targetList.add(new AccidentRoad(
                            idCounter++,
                            road.getStreet(),
                            road.getTimestamp(),
                            road.getTrafficLevel(),
                            accident.getVehicles()));
                }
            }
        }
        return targetList;
    }

    /**
     * Проверка Road и Accident на совпадение по полям timestamp и street
     * @param road дорога
     * @param accident ДТП
     * @return да/нет
     */
    private boolean checkStreetAndTimeStamp(Road road, Accident accident) {
        return road.getTimestamp().equals(accident.getTimestamp()) && road.getStreet().equals(accident.getStreet());
    }

    public List<AccidentRoad> getTargetList() {
        return targetList;
    }
}
