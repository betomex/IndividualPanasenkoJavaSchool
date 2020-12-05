package deserialization.converter;

import model.Accident;
import deserialization.lists.Accidents;
import model.Road;
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
 * Тестирование корректности десериализации списков объектов Road и Accident
 */
public class JacksonConverterTest {
    private static final String XML_DIRECTORY = "src/test/resources/xml";
    private Road road1;
    private Road road2;
    private Accident accident1;
    private Accident accident2;

    public void init() {
        road1 = new Road(
                "Улица 1",
                LocalDateTime.of(2020, 11, 27, 8, 0),
                9);
        road2 = new Road(
                "Улица 2",
                LocalDateTime.of(2020, 11, 27, 10, 0),
                5);
        accident1 = new Accident(
                "Улица 1",
                LocalDateTime.of(2020, 11, 27, 8, 0),
                4);
        accident2 = new Accident(
                "Улица 2",
                LocalDateTime.of(2020, 11, 27, 10, 0),
                2);
    }

    @Test
    public void fromXmlConvertingTest() throws IOException {
        init();
        JacksonConverter converter = new JacksonConverter();
        Path roadsPath = Paths.get(XML_DIRECTORY, "TestRoads.xml");
        Path accidentsPath = Paths.get(XML_DIRECTORY, "TestAccidents.xml");
        String roadsXml = String.join("\n", Files.readAllLines(roadsPath));
        String accidentsXml = String.join("\n", Files.readAllLines(accidentsPath));

        List<Road> roads1 = Arrays.asList(road1, road2);
        List<Accident> accidents1 = Arrays.asList(accident1, accident2);
        Roads roads = converter.fromXml(roadsXml, Roads.class);
        Accidents accidents = converter.fromXml(accidentsXml, Accidents.class);

        Assertions.assertTrue(roads1.containsAll(roads.getRoads()));
        Assertions.assertEquals(roads1.size(), roads.getRoads().size());
        Assertions.assertTrue(accidents1.containsAll(accidents.getAccidents()));
        Assertions.assertEquals(accidents1.size(), accidents.getAccidents().size());
    }
}
