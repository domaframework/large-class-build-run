SELECT /*%expand*/*
  FROM employee e
       LEFT JOIN employee m
              ON e.manager_id = m.id
       INNER JOIN department d
               ON e.department_id = d.id
 WHERE e.id = /*id*/0 
