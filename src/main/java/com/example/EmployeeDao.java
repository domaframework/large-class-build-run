package com.example;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

@Dao
public interface EmployeeDao {
  @Insert
  int insert(Employee entity);

  @Update
  int update(Employee entity);

  @Delete
  int delete(Employee entity);

  @Select(aggregateStrategy = EmployeeAggregateStrategy.class)
  Employee selectById(Long id);

  @Select
  List<Employee> selectByCondition(EmployeeCondition condition);

  @Script
  void create();

  @Script
  void drop();
}
