package com.example;

import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;

import java.util.function.BiConsumer;

@AggregateStrategy(root = Employee.class, tableAlias = "e")
public interface EmployeeAggregateStrategy {

  @AssociationLinker(propertyPath = "department", tableAlias = "d")
  BiConsumer<Employee, Department> department =
      (e, d) -> {
        e.department = d;
        d.employees.add(e);
      };
}
