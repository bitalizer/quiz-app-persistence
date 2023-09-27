package org.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.quiz.exception.DatabaseException;

/**
 * Abstract base class for JDBC-based DAOs.
 *
 * @param <T> The type of entity.
 * @param <K> The type of entity's primary key.
 */
public abstract class AbstractJdbcDao<T, K> implements GenericDao<T, K> {

  private static final int UPDATE_EXECUTED_SUCCESSFULLY = 1;

  private final DataSource dataSource;

  protected AbstractJdbcDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Get a database connection from the data source.
   *
   * @return A database connection.
   * @throws SQLException If a database error occurs while establishing the connection.
   */
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * Get the SQL query to save an object into the database.
   *
   * @param object The object to save.
   * @return The SQL query for saving the object.
   */
  protected abstract String getSaveQuery(T object);

  /**
   * Get the SQL query to update an existing object in the database.
   *
   * @return The SQL query for updating an object.
   */
  protected abstract String getUpdateQuery();

  /**
   * Get the SQL query to retrieve an object by its primary key.
   *
   * @return The SQL query for retrieving an object by ID.
   */
  protected abstract String getByIdQuery();

  /**
   * Get the SQL query to delete an object from the database.
   *
   * @return The SQL query for deleting an object.
   */
  protected abstract String getDeleteQuery();

  /**
   * Set the ID parameter in a prepared statement.
   *
   * @param preparedStatement The prepared statement to set the ID parameter for.
   * @param id                The ID value to set in the prepared statement.
   */
  protected abstract void setIdStatement(PreparedStatement preparedStatement, K id);

  /**
   * Set the object's parameters in a prepared statement.
   *
   * @param preparedStatement The prepared statement.
   * @param object            The object to set.
   */
  protected abstract void setObjectStatement(PreparedStatement preparedStatement, T object);

  /**
   * Read and construct an object from a ResultSet.
   *
   * @param resultSet The ResultSet containing data.
   * @return The constructed object.
   */
  protected abstract T readObject(ResultSet resultSet);

  /**
   * {@inheritDoc}
   */
  public void save(T object) {
    String createQuery = getSaveQuery(object);

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(createQuery)) {

      setObjectStatement(statement, object);

      if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
        throw new DatabaseException("Failed to save the object!");
      }

    } catch (SQLException e) {
      throw new DatabaseException("Database error while saving object", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Optional<T> getById(K id) {
    String selectByIdQuery = getByIdQuery();

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(selectByIdQuery)) {

      setIdStatement(statement, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        T object = readObject(resultSet);
        return Optional.of(object);
      }

    } catch (SQLException e) {
      throw new DatabaseException("Database error while fetching object by ID", e);
    }

    return Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  public void update(T object) {
    String updateQuery = getUpdateQuery();

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(updateQuery)) {

      setObjectStatement(statement, object);

      if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
        throw new DatabaseException("Failed to update the object");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Database error while updating object", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void deleteById(K id) {
    String deleteQuery = getDeleteQuery();

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

      setIdStatement(statement, id);
      if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
        throw new DatabaseException("Failed to delete the object");
      }

    } catch (SQLException e) {
      throw new DatabaseException("Database error while deleting object", e);
    }
  }
}