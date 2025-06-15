SELECT /*%expand*/*
  FROM employee e
 WHERE e.age > /* condition.age */0
   AND e.name LIKE /* condition.namePattern */'test' 
