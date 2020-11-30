package deserialization.deserialized_objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Список дорог с траффиком
 */
public class Roads {

    /**
     * Список дорог
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "road")
    private List<Road> roads;

    /**
     * Список дорог
     * @param roads дороги
     */
    public Roads(List<Road> roads) {
        this.roads = roads;
    }

    public Roads() {}

    public List<Road> getRoads() {
        return roads;
    }
}
