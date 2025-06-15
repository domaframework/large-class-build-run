package com.example;

import java.util.Objects;

public record EmployeeCondition(String namePattern, int age) {
  public EmployeeCondition {
    Objects.requireNonNull(namePattern);
  }
}
