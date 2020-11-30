package database.repository;

import database.model.AccidentRoad;
import database.provider.DataSourceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Тестирование работы с БД
 */
public class AccidentRoadRepositoryTest {
    private AccidentRoadRepository repository;
    private AccidentRoad accidentRoad1;
    private AccidentRoad accidentRoad2;

    @BeforeEach
    public void init() throws IOException {
        DataSourceProvider dataSourceProvider = new DataSourceProvider();
        repository = new AccidentRoadRepository(dataSourceProvider.getDataSource());
        accidentRoad1 = new AccidentRoad(
                1,
                "Улица 2",
                LocalDateTime.of(2020, 11, 27, 10, 0),
                6,
                2);
        accidentRoad2 = new AccidentRoad(
                2,
                "Улица 3",
                LocalDateTime.of(2020, 11,27,23,0),
                2,
                3);
    }

    @AfterEach
    public void clear() {
        repository.dropTable();
    }

    @Test
    public void testAddDataIntoDb() {
        repository.add(accidentRoad1);
        List<AccidentRoad> accidentRoads = repository.findAll();
        Assertions.assertEquals(1, accidentRoads.size());
        Assertions.assertEquals(accidentRoad1, accidentRoads.get(0));

        repository.add(accidentRoad2);
        accidentRoads = repository.findAll();
        Assertions.assertEquals(2, accidentRoads.size());
        Assertions.assertEquals(accidentRoad2, accidentRoads.get(1));
    }
}
