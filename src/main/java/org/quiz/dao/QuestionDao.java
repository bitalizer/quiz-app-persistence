package org.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.quiz.exception.DatabaseException;
import org.quiz.model.Question;
import org.quiz.model.Topic;

/**
 * An implementation of {@link QuestionDao} that persists questions in database.
 */
public class QuestionDao extends AbstractJdbcDao<Question, Long> {

  /**
   * Creates an instance of {@link QuestionDao} which uses provided <code>dataSource</code>
   * to store and retrieve question data.
   *
   * @param dataSource a non-null dataSource.
   */
  public QuestionDao(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  protected String getSaveQuery(Question object) {
    return "INSERT INTO questions (difficulty, content, topic_id, id) VALUES (?,?,?,?);";
  }

  @Override
  protected String getUpdateQuery() {
    return "UPDATE questions SET difficulty = ?, content = ?, topic_id = ? WHERE id = ?;";
  }

  @Override
  protected String getByIdQuery() {
    return "SELECT questions.*, topics.id AS topic_id, topics.name AS topic_name " +
        "FROM questions " +
        "LEFT JOIN topics ON questions.topic_id = topics.id " +
        "WHERE questions.id = ?;";
  }

  protected String getFindByTopicQuery() {
    return "SELECT questions.*, topics.id AS topic_id, topics.name AS topic_name " +
        "FROM questions " +
        "LEFT JOIN topics ON questions.topic_id = topics.id " +
        "WHERE topics.name = ?;";
  }

  @Override
  protected String getDeleteQuery() {
    return "DELETE FROM questions WHERE id = ?";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setIdStatement(PreparedStatement statement, Long id) {
    try {
      statement.setLong(1, id);
    } catch (SQLException e) {
      throw new DatabaseException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setObjectStatement(PreparedStatement statement, Question question) {
    try {

      if (question.getTopic() == null || question.getTopic().getId() == null) {
        throw new IllegalArgumentException("Question must have a valid topic");
      }

      statement.setInt(1, question.getDifficulty());
      statement.setString(2, question.getContent());
      statement.setLong(3, question.getTopic().getId());

      if (question.getId() != null) {
        statement.setLong(4, question.getId());
      }

    } catch (SQLException e) {
      throw new DatabaseException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Question readObject(ResultSet resultSet) {

    try {
      Topic topic = Topic.builder()
          .id(resultSet.getLong("topic_id"))
          .name(resultSet.getString("topic_name"))
          .build();

      return Question.builder()
          .id(resultSet.getLong("id"))
          .difficulty(resultSet.getInt("difficulty"))
          .content(resultSet.getString("content"))
          .topic(topic)
          .build();
    } catch (SQLException e) {
      throw new DatabaseException(e.getMessage(), e);
    }
  }

  /**
   * Retrieves a list of questions associated with a specific topic.
   *
   * @param topicName The name of the topic to search for questions.
   * @return A list of questions related to the specified topic.
   * @throws DatabaseException If a database error occurs while fetching questions.
   */
  public List<Question> findByTopic(String topicName) {

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(getFindByTopicQuery())) {

      statement.setString(1, topicName);
      ResultSet resultSet = statement.executeQuery();

      List<Question> questions = new ArrayList<>();

      while (resultSet.next()) {
        Topic topic = Topic.builder()
            .id(resultSet.getLong("topic_id"))
            .name(resultSet.getString("topic_name"))
            .build();

        Question question = Question.builder()
            .id(resultSet.getLong("id"))
            .difficulty(resultSet.getInt("difficulty"))
            .content(resultSet.getString("content"))
            .topic(topic)
            .build();

        questions.add(question);
      }

      return questions;
    } catch (SQLException e) {
      throw new DatabaseException("Error while fetching questions by topic", e);
    }
  }
}