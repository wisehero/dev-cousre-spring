package programmers.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_SQL = "select * from customers WHERE name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private final String UPDATE_BY_ID_SQL = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BIN(?)";
    private final String DELETE_ALL_SQL = "DELETE from customers";

    public List<String> findNames(String name) {

        List<String> names = new ArrayList<>();

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
//             var statement = connection.createStatement(); // SQL 인젝션 발생 가능성 있음
             var statement = connection.prepareStatement(SELECT_SQL);
        ) {
            statement.setString(1, name);
            try (var resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, name -> {}, createdAt -> {}", customerId, name, created_at);
                    names.add(customerName);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }

        return names;
    }

    public List<String> findAllNames() {

        List<String> names = new ArrayList<>();

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
//             var statement = connection.createStatement(); // SQL 인젝션 발생 가능성 있음
             var statement = connection.prepareStatement(SELECT_ALL_SQL);
             var resultSet = statement.executeQuery();
        ) {

            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
                logger.info("customer id -> {}, name -> {}, createdAt -> {}", customerId, customerName, created_at);
                names.add(customerName);
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }

        return names;
    }

    public int insertCustomer(UUID customerId, String name, String email) {

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
             var statement = connection.prepareStatement(INSERT_SQL);
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public int updateCustomerName(UUID customerId, String name) {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public List<UUID> findAllIds() {
        List<UUID> uuids = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                uuids.add(customerId);
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return uuids;
    }

    public int deleteAllCustomers() {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "xngosem258!");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ) {
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public static void main(String[] args) throws SQLException {

        var customerRepository = new JdbcCustomerRepository();

        customerRepository.deleteAllCustomers();

        customerRepository.insertCustomer(UUID.randomUUID(), "new-user", "new-user@gmail.com");
        var customer2 = UUID.randomUUID();
        customerRepository.insertCustomer(customer2, "new-user2", "new-use2r@gmail.com");
        customerRepository.updateCustomerName(customer2, "updated-user2");

//        var names = customerRepository.findAllNames();
//        names.forEach(v -> logger.info("Found name : {}", v));

    }
}

