package combine;

import database.model.AccidentRoad;
import deserialization.converter.JacksonConverter;
import deserialization.deserialized_objects.Accidents;
import deserialization.deserialized_objects.Roads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Тестирование корректности выделения объектов AccidentRoad (траффик на дороге в момент дтп) из Road и Accident
 */
public class CombineXmlEntitiesTest {
    private static final String XML_DIRECTORY = "src/main/resources/xml";
    private AccidentRoad accidentRoad1;
    private AccidentRoad accidentRoad2;

    public void init() throws IOException {
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

    @Test
    public void combineTest() throws IOException {
        init();
        JacksonConverter converter = new JacksonConverter();
        Path roadsPath = Paths.get(XML_DIRECTORY, "Roads.xml");
        Path accidentsPath = Paths.get(XML_DIRECTORY, "Accidents.xml");
        String roadsXml = Files.readString(roadsPath);
        String accidentsXml = Files.readString(accidentsPath);

        Roads roads = converter.fromXml(roadsXml, Roads.class);
        Accidents accidents = converter.fromXml(accidentsXml, Accidents.class);

        RoadAccidentCombiner combiner = new RoadAccidentCombiner(roads.getRoads(), accidents.getAccidents());
        List<AccidentRoad> combinedAccidentRoads = combiner.combine();
        List<AccidentRoad> expectedAccidentRoads = Arrays.asList(accidentRoad1, accidentRoad2);

        Assertions.assertTrue(combinedAccidentRoads.containsAll(expectedAccidentRoads));
        Assertions.assertEquals(expectedAccidentRoads.size(), combinedAccidentRoads.size());
    }
}
