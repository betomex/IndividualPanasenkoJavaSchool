package database.provider;

import org.apache.derby.jdbc.EmbeddedDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Датасорс
 */
public class DataSourceProvider {

    /**
     * Датасорс
     */
    private EmbeddedDataSource dataSource;

    /**
     * Настройки для нашей дб из settings.properties
     */
    private Map<String, String> properties = new HashMap<>();

    /**
     * Загрузка свойств бд из файла
     * @throws IOException ошибка загрузки
     */
    public DataSourceProvider() throws IOException {
        loadProperties();
    }

    /**
     * Загрузка настроек дб в память
     * @throws IOException ошибка загрузки
     */
    private void loadProperties() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("settings.properties"));
            for (Map.Entry<Object, Object> entry: properties.entrySet()) {
                this.properties.put((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Loading properties error");
            throw e;
        }
    }

    /**
     * Геттер для датасорса
     * @return датасорс
     */
    public EmbeddedDataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new EmbeddedDataSource();
            dataSource.setUser("");
            dataSource.setPassword("");
            dataSource.setDatabaseName(properties.get("dbname"));
            dataSource.setCreateDatabase("create");
        }
        return dataSource;
    }
}
