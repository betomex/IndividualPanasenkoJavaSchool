package deserialization.lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import model.Accident;

import java.util.List;

/**
 * Список ДТП
 */
@JacksonXmlRootElement
public class Accidents {

    /**
     * Список ДТП
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "accident")
    private List<Accident> accidents;

    /**
     * Список ДТП
     * @param accidents дтп
     */
    public Accidents(List<Accident> accidents) {
        this.accidents = accidents;
    }

    public Accidents() {}

    public List<Accident> getAccidents() {
        return accidents;
    }
}
