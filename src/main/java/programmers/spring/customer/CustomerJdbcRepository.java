package programmers.spring.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {

    private final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepository.class);
    private final DataSource dataSource;

    public CustomerJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer insert(Customer customer) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?,?,?)")
        ) {
            statement.setBytes(1, customer.getCustomer_id().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
            statement.executeUpdate();
            return customer;
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public Customer update(Customer customer) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)");) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setTimestamp(3, customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
            statement.setBytes(4, customer.getCustomer_id().toString().getBytes());
            var executable = statement.executeUpdate();
            if (executable != 1) {
                throw new RuntimeException("Nothing was updated");
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers");
                var resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                mapToCustomer(allCustomers, resultSet);
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }

        return allCustomers;
    }

    private void mapToCustomer(List<Customer> allCustomers, ResultSet resultSet) throws SQLException {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        List<Customer> allToCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE customer_id=UUID_TO_BIN(?)");
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allToCustomer, resultSet);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }
        return allToCustomer.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allToCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE name=?");
        ) {
            statement.setString(1, name);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allToCustomer, resultSet);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }
        return allToCustomer.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allToCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE email=?");
        ) {
            statement.setString(1, email);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allToCustomer, resultSet);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }
        return allToCustomer.stream().findFirst();
    }

    @Override
    public void deleteAll() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ) {
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
            throw new RuntimeException(throwables);
        }

    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
