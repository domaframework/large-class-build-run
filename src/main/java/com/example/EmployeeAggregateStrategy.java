package com.example;

import java.util.function.BiConsumer;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;

@AggregateStrategy(root = Employee.class, tableAlias = "e")
public interface EmployeeAggregateStrategy {

  @AssociationLinker(propertyPath = "manager", tableAlias = "m")
  BiConsumer<Employee, Employee> manager = (e, m) -> e.manager = m;

  @AssociationLinker(propertyPath = "department", tableAlias = "d")
  BiConsumer<Employee, Department> department =
      (e, d) -> {
        e.department = d;
        d.employees.add(e);
      };
}
