package database.repository;

import database.converter.LocalDateTimeConverter;
import database.init.TableInitializer;
import model.AccidentRoad;
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
    private static final String TABLE_NAME = AccidentRoad.getTableName();

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
        new TableInitializer().createAccidentRoadTable(dataSource);
    }

    /**
     * Добавление новой записи в таблицу
     * @param accidentRoad модель
     */
    public void createNew(AccidentRoad accidentRoad) {
        String sqlQuery = "INSERT INTO " + TABLE_NAME + "(street, date, time, traffic_level, vehicles) VALUES(?, ?, ?, ?, ?)";
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
        statement.setString(1, accidentRoad.getStreet());
        statement.setDate(2, converter.extractDate(accidentRoad.getTimestamp()));
        statement.setTime(3, converter.extractTime(accidentRoad.getTimestamp()));
        statement.setInt(4, accidentRoad.getTrafficLevel());
        statement.setInt(5, accidentRoad.getVehicles());
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


    public AccidentRoad findById(Integer id) {
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
        List<AccidentRoad> listFromResultSet = Collections.emptyList();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            listFromResultSet = createListFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
        return listFromResultSet.isEmpty() ? null : listFromResultSet.get(0);
    }


    public void update(AccidentRoad accidentRoad) {
        String sqlQuery =
                "UPDATE " + TABLE_NAME
                        + " SET street = ?, date = ?, time = ?, traffic_level = ?, vehicles = ?"
                        + " WHERE id = " + accidentRoad.getId().toString();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)
        ) {
            prepareUpdateStatement(statement, accidentRoad);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error while updating data in table " + TABLE_NAME);
        }
    }


    private void prepareUpdateStatement(PreparedStatement statement, AccidentRoad accidentRoad) throws SQLException {
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        statement.setString(1, accidentRoad.getStreet());
        statement.setDate(2, converter.extractDate(accidentRoad.getTimestamp()));
        statement.setTime(3, converter.extractTime(accidentRoad.getTimestamp()));
        statement.setInt(4, accidentRoad.getTrafficLevel());
        statement.setInt(5, accidentRoad.getVehicles());
    }


    public void delete(Integer id) {
        String sqlQuery = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error while deleting data from table " + e.getMessage());
        }
    }
}
