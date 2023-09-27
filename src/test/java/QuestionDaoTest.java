import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quiz.dao.QuestionDao;
import org.quiz.model.Question;
import org.quiz.model.Topic;
import org.zapodot.junit.db.annotations.EmbeddedDatabase;
import org.zapodot.junit.db.annotations.EmbeddedDatabaseTest;
import org.zapodot.junit.db.common.CompatibilityMode;
import org.zapodot.junit.db.common.Engine;

@EmbeddedDatabaseTest(
    engine = Engine.HSQLDB,
    compatibilityMode = CompatibilityMode.MySQL,
    initialSqlResources = {"classpath:schema.sql", "classpath:test-data.sql"}
)
@ExtendWith(MockitoExtension.class)
class QuestionDaoTest {

  private static QuestionDao questionDao;

  @BeforeEach
  void setUp(final @EmbeddedDatabase DataSource dataSource) {
    questionDao = new QuestionDao(dataSource);
  }

  @Test
  void shouldGetQuestionById() {
    Optional<Question> actualQuestion = questionDao.getById(2L);
    assertTrue(actualQuestion.isPresent());

    // Verify the content of the retrieved question
    assertEquals(2L, actualQuestion.get().getId());
    assertEquals(3, actualQuestion.get().getDifficulty());
    assertEquals("Who discovered penicillin?", actualQuestion.get().getContent());
    assertEquals(2L, actualQuestion.get().getTopic().getId());
    assertEquals("Science", actualQuestion.get().getTopic().getName());
  }

  @Test
  void shouldReturnEmptyOptionalForMissingQuestion() {
    Optional<Question> actualQuestion = questionDao.getById(123456L);
    assertTrue(actualQuestion.isEmpty());
  }

  @Test
  void shouldSaveQuestion() {
    // Prepare a Question object
    Question question = Question.builder()
        .id(555L)
        .difficulty(2)
        .content("Sample question")
        .topic(Topic.builder().id(1L).build())
        .build();

    // Call the save method
    questionDao.save(question);

    // Verify that the question was saved
    Optional<Question> savedQuestion = questionDao.getById(question.getId());
    assertTrue(savedQuestion.isPresent());
    assertEquals(question.getDifficulty(), savedQuestion.get().getDifficulty());
    assertEquals(question.getContent(), savedQuestion.get().getContent());
    assertEquals(question.getTopic().getId(), savedQuestion.get().getTopic().getId());
    assertFalse(savedQuestion.get().getTopic().getName().isBlank());
  }

  @Test
  void shouldUpdateQuestion() {
    // Get an existing question
    Optional<Question> existingQuestion = questionDao.getById(4L);
    assertTrue(existingQuestion.isPresent());

    // Update the difficulty of the question
    Question questionToUpdate = existingQuestion.get();
    questionToUpdate.setDifficulty(10);
    questionToUpdate.setContent("New content");
    questionToUpdate.setTopic(Topic.builder().id(3L).build());

    // Call the update method
    questionDao.update(questionToUpdate);

    // Verify that the question was updated
    Optional<Question> updatedQuestionResult = questionDao.getById(questionToUpdate.getId());
    assertTrue(updatedQuestionResult.isPresent());
    assertEquals(questionToUpdate.getDifficulty(), updatedQuestionResult.get().getDifficulty());
    assertEquals(questionToUpdate.getContent(), updatedQuestionResult.get().getContent());
    assertEquals("History", updatedQuestionResult.get().getTopic().getName());
  }

  @Test
  void shouldDeleteQuestion() {

    Long existingQuestionId = 2L;

    // Verify that the question was saved
    Optional<Question> savedQuestion = questionDao.getById(existingQuestionId);
    assertTrue(savedQuestion.isPresent());

    // Delete the question
    questionDao.deleteById(existingQuestionId);

    // Verify that the question was deleted
    Optional<Question> deletedQuestion = questionDao.getById(existingQuestionId);
    assertFalse(deletedQuestion.isPresent());
  }

  @Test
  void shouldGetQuestionsByTopic() {
    List<Question> questions = questionDao.findByTopic("Geography");
    assertEquals(1, questions.size());
  }

}