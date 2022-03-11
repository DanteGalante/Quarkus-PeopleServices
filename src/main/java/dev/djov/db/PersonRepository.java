package dev.djov.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import javax.enterprise.context.ApplicationScoped;

import dev.djov.Person;

@ApplicationScoped
public class PersonRepository {

  private static final String GET_ALL = "SELECT * FROM allpeople";
  private static final String FIND_BY_ID = "SELECT * FROM people WHERE id = ?";
  private static final String INSERT = "INSERT INTO people (id, name, age) VALUES (?, ?, ?)";

  private final DataSource dataSource;

  public PersonRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<Person> findAll() {
    List<Person> result = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        result.add(
            new Person(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                resultSet.getInt("age")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public Person findById(UUID id) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
        statement.setString(1, id.toString());
      System.out.println(statement.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return new Person(
              UUID.fromString(resultSet.getString("id")),
              resultSet.getString("name"),
              resultSet.getInt("age"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Person insert(Person person) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT)) {
      statement.setObject(1, person.getId());
      statement.setString(2, person.getName());
      statement.setInt(3, person.getAge());
      System.out.println("hola, esto es el insert");
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return person;
  }

  public Person update(Person person) {
    try (Connection connection = dataSource.getConnection();
    CallableStatement cstm = connection.prepareCall("CALL update_people(?,?,?)")) {
      cstm.setString(1, person.getId().toString());
      cstm.setString(2, person.getName());
      cstm.setInt(3, person.getAge());
      cstm.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return person;
  }
}