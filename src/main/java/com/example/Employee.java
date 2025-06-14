package com.example;

import com.example.domain.Name;
import java.net.URL;
import java.time.LocalDate;
import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public Name name;
  public Integer age;
  public LocalDate birthday;
  public URL profileUrl;
  public String description1;
  public String description2;
  public String description3;
  public String description4;
  public String description5;
  public String description6;
  public String description7;
  public String description8;
  public String description9;
  public String description10;
  public Long managerId;
  public Long departmentId;
  @Version public Integer version;
  @Association public Employee manager;
  @Association public Department department;
}
