package database.init;

import model.AccidentRoad;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Класс-инициализатор сущностей базы данных.
 */
public class TableInitializer {

    /** Имя таблицы состояния траффика на дороге, где произошло ДТП, в данный момент времени */
    private static final String ACCIDENT_ROAD_TABLE_NAME = AccidentRoad.getTableName();

    /** Запрос на создание таблицы состояния траффика на дороге, где произошло ДТП, в данный момент времени */
    private static final String ACCIDENT_ROAD_QUERY =
            "CREATE TABLE " + ACCIDENT_ROAD_TABLE_NAME + "("
                    + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1), "
                    + "street VARCHAR(255), "
                    + "date DATE, "
                    + "time TIME, "
                    + "traffic_level INTEGER, "
                    + "vehicles INTEGER)";

    /**
     * Создание таблицы состояния траффика на дороге, где произошло ДТП, в данный момент времени
     * @param dataSource - DataSource.
     */
    public void createAccidentRoadTable(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement())
        {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(
                    null, null, ACCIDENT_ROAD_TABLE_NAME.toUpperCase(), new String[] {"TABLE"});
            if (!resultSet.next()) {
                statement.executeUpdate(ACCIDENT_ROAD_QUERY);
            }
        } catch (SQLException e) {
            System.out.println("Table initialize error " + ACCIDENT_ROAD_TABLE_NAME);
        }
    }
}
