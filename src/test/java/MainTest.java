import combine.RoadAccidentCombiner;
import database.provider.DataSourceProvider;
import database.repository.AccidentRoadRepository;
import deserialization.converter.JacksonConverter;
import deserialization.lists.Accidents;
import deserialization.lists.Roads;
import model.AccidentRoad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Тестирование всей работы:
 * -- десериализация списков объектов из XML файлов
 * -- выделение объектов для новой сущности из полученных списков
 * -- запись списка полученных объектов в БД
 * -- извлечение объектов из БД для проверки корректности
 */
public class MainTest {
    private static final String XML_DIRECTORY = "src/test/resources/xml";
    private AccidentRoadRepository repository;

    @BeforeEach
    public void init() throws IOException {
        DataSourceProvider dataSourceProvider = new DataSourceProvider();
        repository = new AccidentRoadRepository(dataSourceProvider.getDataSource());
    }

    @Test
    public void fullWorkTest() throws IOException {
        // 1. Десериализация
        JacksonConverter converter = new JacksonConverter();
        Path roadsPath = Paths.get(XML_DIRECTORY, "Roads.xml");
        Path accidentsPath = Paths.get(XML_DIRECTORY, "Accidents.xml");
        String roadsXml = String.join("\n", Files.readAllLines(roadsPath));
        String accidentsXml = String.join("\n", Files.readAllLines(accidentsPath));

        Roads roads = converter.fromXml(roadsXml, Roads.class);
        Accidents accidents = converter.fromXml(accidentsXml, Accidents.class);

        // 2. Выделение в новую сущность
        RoadAccidentCombiner combiner = new RoadAccidentCombiner(roads.getRoads(), accidents.getAccidents());
        List<AccidentRoad> combinedAccidentRoads = combiner.combine();

        int initialSize = repository.findAll().size();

        // 3. Добавление в БД
        for (AccidentRoad accidentRoad: combinedAccidentRoads) {
            repository.createNew(accidentRoad);
        }

        // 4. Проверка корректности
        List<AccidentRoad> accidentRoadsFromDb = repository.findAll();
        Assertions.assertEquals(initialSize + combinedAccidentRoads.size(), accidentRoadsFromDb.size());
    }
}
