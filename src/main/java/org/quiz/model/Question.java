package org.quiz.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Represents a question in a quiz.
 */
@Data
@Builder
@ToString
public class Question {

  private Long id;

  private Integer difficulty;

  private String content;

  private Topic topic;
}
