package database.repository;

import database.converter.LocalDateTimeConverter;
import database.model.AccidentRoad;
import org.apache.derby.jdbc.EmbeddedDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Репозиторий для работы с таблицей accident_road
 */
public class AccidentRoadRepository {

    /**
     * Название таблицы
     */
    private static final String TABLE_NAME = "accident_road";

    /**
     * SQL запрос на создание таблицы
     */
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + "("
                    + "id INTEGER PRIMARY KEY, "
                    + "street VARCHAR(255), "
                    + "date DATE, "
                    + "time TIME, "
                    + "traffic_level INTEGER, "
                    + "vehicles INTEGER)";

    /**
     * датасорс
     */
    private EmbeddedDataSource dataSource;

    /**
     * Инициализация таблицы
     * @param dataSource датасорс
     */
    public AccidentRoadRepository(EmbeddedDataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    /**
     * Инициализация таблицы
     */
    private void initTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(
                    null, null, TABLE_NAME.toUpperCase(), new String[] {"TABLE"});
            if (!resultSet.next()) {
                statement.executeUpdate(CREATE_TABLE_QUERY);
            }
        } catch (SQLException e) {
            System.out.println("Table initialize error " + TABLE_NAME);
        }
    }

    /**
     * Добавление новой записи в таблицу
     * @param accidentRoad модель
     */
    public void add(AccidentRoad accidentRoad) {
        String sqlQuery = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            prepareAddStatement(statement, accidentRoad);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Adding a new row into " + TABLE_NAME + " error");
        }
    }

    /**
     * Подготовка запроса добавления новой записи в таблицу
     * @param statement запрос
     * @param accidentRoad модель
     * @throws SQLException ошибка подготовки запроса
     */
    private void prepareAddStatement(PreparedStatement statement, AccidentRoad accidentRoad) throws SQLException {
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        statement.setInt(1, accidentRoad.getId());
        statement.setString(2, accidentRoad.getStreet());
        statement.setDate(3, converter.extractDate(accidentRoad.getTimestamp()));
        statement.setTime(4, converter.extractTime(accidentRoad.getTimestamp()));
        statement.setInt(5, accidentRoad.getTrafficLevel());
        statement.setInt(6, accidentRoad.getVehicles());
    }

    /**
     * Получение всех записей таблицы
     * @return список из записей таблицы
     */
    public List<AccidentRoad> findAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            return createListFromResultSet(statement.executeQuery("SELECT * FROM " + TABLE_NAME));
        } catch (SQLException e) {
            System.out.println("Select rows from " + TABLE_NAME + " error");
        }
        return Collections.emptyList();
    }

    /**
     * Создание списка из объектов ResultSet
     * @param resultSet резалтсет
     * @return список объектов ResultSet
     * @throws SQLException ошибка обращения к резалтсет
     */
    private List<AccidentRoad> createListFromResultSet(ResultSet resultSet) throws SQLException {
        List<AccidentRoad> accidentRoads = new ArrayList<>();
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        while (resultSet.next()) {
            LocalDateTime timestamp =
                    converter.fromDb(resultSet.getDate("date"), resultSet.getTime("time"));
            accidentRoads.add(new AccidentRoad(
                    resultSet.getInt("id"),
                    resultSet.getString("street"),
                    timestamp,
                    resultSet.getInt("traffic_level"),
                    resultSet.getInt("vehicles")));
        }
        return accidentRoads;
    }

    /**
     * Удаление таблицы
     */
    public void dropTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + TABLE_NAME);
        } catch (SQLException e) {
            System.out.println("Drop table error: " + e.getMessage());
        }
    }
}
