package org.quiz.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Quiz {

  private Long id;

  private String name;

  private List<Question> questions;
}
