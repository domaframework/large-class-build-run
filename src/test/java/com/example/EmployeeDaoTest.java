package com.example;

import static org.junit.jupiter.api.Assertions.*;

import com.example.domain.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SimpleConfig;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

class EmployeeDaoTest {

  private final Config config =
      SimpleConfig.builder("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
          .naming(Naming.SNAKE_LOWER_CASE)
          .jdbcLogger(new Slf4jJdbcLogger())
          .build();

  private final EmployeeDao employeeDao = new EmployeeDaoImpl(config);

  @BeforeEach
  void setup() {
    employeeDao.drop();
    employeeDao.create();
  }

  @Test
  void insert() {
    var employee = new Employee();
    employee.name = new Name("ABC");
    employee.departmentId = 2L;
    var result = employeeDao.insert(employee);
    assertEquals(1, result);
  }

  @Test
  void update() {
    var employee = employeeDao.selectById(2L);
    employee.name = new Name("ABC");
    var result = employeeDao.update(employee);
    assertEquals(1, result);
  }

  @Test
  void delete() {
    var employee = employeeDao.selectById(2L);
    var result = employeeDao.delete(employee);
    assertEquals(1, result);
  }

  @Test
  void selectById() {
    var employee = employeeDao.selectById(1L);
    assertNotNull(employee);
    assertNotNull(employee.name);
    assertNotNull(employee.department);
    assertEquals("John Smith", employee.name.value());
    assertEquals("Engineering", employee.department.name.value());
  }
}
