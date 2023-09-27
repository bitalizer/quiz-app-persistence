package org.quiz.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Topic {

  private Long id;

  private String name;

}
