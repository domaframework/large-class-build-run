package com.example;

import com.example.domain.Name;
import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
public class Department {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public Name name;
  @Version public Integer version;
  @Association public List<Employee> employees = new ArrayList<>();
}
