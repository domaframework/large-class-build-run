SELECT /*%expand*/*
  FROM employee e
       INNER JOIN department d
               ON e.department_id = d.id
 WHERE e.id = /*id*/0 
