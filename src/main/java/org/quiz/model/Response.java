package org.quiz.model;

import lombok.Data;

@Data
public class Response {

  private Long id;

  private Long questionId;

  private boolean isCorrect;

  private String text;

}
