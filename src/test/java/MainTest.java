import combine.RoadAccidentCombiner;
import database.model.AccidentRoad;
import database.provider.DataSourceProvider;
import database.repository.AccidentRoadRepository;
import deserialization.converter.JacksonConverter;
import deserialization.deserialized_objects.Accidents;
import deserialization.deserialized_objects.Roads;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Тестирование всей работы:
 * -- десериализация списков объектов из XML файлов
 * -- выделение объектов для новой сущности из полученных списков
 * -- запись списка полученных объектов в БД
 * -- извлечение объектов из БД для проверки корректности
 */
public class MainTest {
    private static final String XML_DIRECTORY = "src/main/resources/xml";
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
                LocalDateTime.of(2020, 11, 27, 23, 0),
                2,
                3);
    }

    @AfterEach
    public void clear() {
        repository.dropTable();
    }

    @Test
    public void fullWorkTest() throws IOException {
        // 1. Десериализация
        JacksonConverter converter = new JacksonConverter();
        Path roadsPath = Paths.get(XML_DIRECTORY, "Roads.xml");
        Path accidentsPath = Paths.get(XML_DIRECTORY, "Accidents.xml");
        String roadsXml = Files.readString(roadsPath);
        String accidentsXml = Files.readString(accidentsPath);

        Roads roads = converter.fromXml(roadsXml, Roads.class);
        Accidents accidents = converter.fromXml(accidentsXml, Accidents.class);

        // 2. Выделение в новую сущность
        RoadAccidentCombiner combiner = new RoadAccidentCombiner(roads.getRoads(), accidents.getAccidents());
        List<AccidentRoad> combinedAccidentRoads = combiner.combine();

        // 3. Добавление в БД
        for (AccidentRoad accidentRoad: combinedAccidentRoads) {
            repository.add(accidentRoad);
        }

        // 4. Проверка корректности
        List<AccidentRoad> accidentRoadsFromDb = repository.findAll();
        List<AccidentRoad> expectedAccidentRoads = Arrays.asList(accidentRoad1, accidentRoad2);
        Assertions.assertTrue(expectedAccidentRoads.containsAll(accidentRoadsFromDb));
        Assertions.assertEquals(expectedAccidentRoads.size(), accidentRoadsFromDb.size());
    }
}
