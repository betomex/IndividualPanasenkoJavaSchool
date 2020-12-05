package combine;

import model.AccidentRoad;
import deserialization.converter.JacksonConverter;
import deserialization.lists.Accidents;
import deserialization.lists.Roads;
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

    private static final String XML_DIRECTORY = "src/test/resources/xml";
    private AccidentRoad accidentRoad1;
    private AccidentRoad accidentRoad2;

    public void init() {
        accidentRoad1 = new AccidentRoad(
                "Улица 2",
                LocalDateTime.of(2020, 11, 27, 10, 0),
                6,
                2);
        accidentRoad2 = new AccidentRoad(
                "Улица 3",
                LocalDateTime.of(2020, 11, 27, 23, 0),
                3,
                2);
    }

    @Test
    public void combineTest() throws IOException {
        init();
        JacksonConverter converter = new JacksonConverter();
        Path roadsPath = Paths.get(XML_DIRECTORY, "Roads.xml");
        Path accidentsPath = Paths.get(XML_DIRECTORY, "Accidents.xml");
        String roadsXml = String.join("\n", Files.readAllLines(roadsPath));
        String accidentsXml = String.join("\n", Files.readAllLines(accidentsPath));

        Roads roads = converter.fromXml(roadsXml, Roads.class);
        Accidents accidents = converter.fromXml(accidentsXml, Accidents.class);

        RoadAccidentCombiner combiner = new RoadAccidentCombiner(roads.getRoads(), accidents.getAccidents());
        List<AccidentRoad> combinedAccidentRoads = combiner.combine();
        List<AccidentRoad> expectedAccidentRoads = Arrays.asList(accidentRoad1, accidentRoad2);

        Assertions.assertEquals(expectedAccidentRoads.size(), combinedAccidentRoads.size());
        for (int i = 0; i < combinedAccidentRoads.size(); i++) {
            Assertions.assertEquals(expectedAccidentRoads.get(i).getStreet(), combinedAccidentRoads.get(i).getStreet());
            Assertions.assertEquals(
                    expectedAccidentRoads.get(i).getTimestamp(),
                    combinedAccidentRoads.get(i).getTimestamp()
            );
            Assertions.assertEquals(
                    expectedAccidentRoads.get(i).getTrafficLevel(),
                    combinedAccidentRoads.get(i).getTrafficLevel()
            );
            Assertions.assertEquals(
                    expectedAccidentRoads.get(i).getVehicles(),
                    combinedAccidentRoads.get(i).getVehicles()
            );
        }
    }
}
