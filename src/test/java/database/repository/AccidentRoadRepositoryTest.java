package database.repository;

import database.provider.DataSourceProvider;
import model.AccidentRoad;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Тестирование работы с БД
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccidentRoadRepositoryTest {

    private AccidentRoadRepository repository;
    private AccidentRoad accidentRoad1 = new AccidentRoad(
            "Улица 1",
            LocalDateTime.of(2020, 11, 27, 10, 0),
            6,
            2
    );

    public AccidentRoadRepositoryTest() throws IOException {
        DataSourceProvider dataSourceProvider = new DataSourceProvider();
        repository = new AccidentRoadRepository(dataSourceProvider.getDataSource());
    }

    @Test
    @Order(1)
    public void testCreateNew() {
        List<AccidentRoad> accidentRoads = repository.findAll();
        int initialSize = accidentRoads.size();
        repository.createNew(accidentRoad1);
        accidentRoads = repository.findAll();
        Assertions.assertEquals(initialSize + 1, accidentRoads.size());
    }

    @Test
    @Order(2)
    public void testFindById() {
        List<AccidentRoad> accidentRoads = repository.findAll();
        AccidentRoad checkedObject = repository.findById(accidentRoads.get(0).getId());
        Assertions.assertEquals(accidentRoads.get(0), checkedObject);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        List<AccidentRoad> accidentRoads = repository.findAll();
        AccidentRoad checkedObject = repository.findById(accidentRoads.get(0).getId());
        int initialVehicles = checkedObject.getVehicles();
        checkedObject.setVehicles(10);
        repository.update(checkedObject);
        checkedObject = repository.findById(accidentRoads.get(0).getId());
        Assertions.assertNotEquals(initialVehicles, checkedObject.getVehicles());
    }

    @Test
    @Order(4)
    public void testDelete() {
        List<AccidentRoad> accidentRoadList = repository.findAll();
        if (!accidentRoadList.isEmpty()) {
            AccidentRoad checkedObject = accidentRoadList.get(0);
            int initialSize = accidentRoadList.size();
            repository.delete(checkedObject.getId());
            accidentRoadList = repository.findAll();
            Assertions.assertEquals(initialSize - 1, accidentRoadList.size());
            Assertions.assertFalse(accidentRoadList.contains(checkedObject));
        }
    }
}
