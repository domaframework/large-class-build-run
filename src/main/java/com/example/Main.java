package com.example;

import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SimpleConfig;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    var config =
        SimpleConfig.builder("jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1")
            .naming(Naming.SNAKE_LOWER_CASE)
            .jdbcLogger(new Slf4jJdbcLogger())
            .build();
    EmployeeDao employeeDao = new EmployeeDaoImpl(config);
    employeeDao.create();
    var employee = employeeDao.selectById(1L);

    Objects.requireNonNull(employee);
    Objects.requireNonNull(employee.name);
    Objects.requireNonNull(employee.department);

    logger.info(employee.name.value()); // John Smith
    logger.info(employee.department.name.value()); // Engineering
  }
}
