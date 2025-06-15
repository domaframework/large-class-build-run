plugins {
    id("java")
    id("application")
    alias(libs.plugins.doma.compile)
    alias(libs.plugins.spotless)
}

group = "com.example"
version = "1.0-SNAPSHOT"

var generationSize = 200
var daoPackagePath = "src/main/java/com/example/dao"
var daoTestPackagePath = "src/test/java/com/example/dao"
var entityPackagePath = "src/main/java/com/example/entity"
var sqlFileDirPath = "src/main/resources/META-INF/com/example/dao"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(libs.doma.core)
    implementation(libs.doma.slf4j)
    implementation(libs.h2.database)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    annotationProcessor(libs.doma.processor)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

application {
    mainClass.set("com.example.Main")
}

spotless {
    java {
        googleJavaFormat(libs.versions.googleJavaFormat.get())
        target("src/*/java/**/*.java")
        targetExclude(
            "**/generated/**",
            "$daoPackagePath/**",
            "$daoTestPackagePath/**",
            "$entityPackagePath/**",
            "$sqlFileDirPath/**",
        )
    }
    kotlin {
        target("*.gradle.kts")
        ktlint()
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        // options.compilerArgs.addAll(listOf("-Adoma.trace=true"))
    }

    clean {
        dependsOn("removeGeneratedFiles")
    }

    register("generateAll") {
        dependsOn("generateDAOs", "generateTests", "generateEntities", "generateSqlFiles")
    }

    register("generateDAOs") {
        dependsOn("generateEntities")
        doLast {
            val sourceDir = file(daoPackagePath)
            sourceDir.mkdirs()

            (1..generationSize).forEach { i ->
                val employeeDaoFile = File(sourceDir, "Employee${i}Dao.java")
                writeEmployeeDaoCode(employeeDaoFile, i)
                val employeeAggregateStrategyFile = File(sourceDir, "Employee${i}AggregateStrategy.java")
                writeEmployeeAggregateStrategyCode(employeeAggregateStrategyFile, i)
            }
            println("Generated DAO files in src/main/java/com/example/dao")
        }
    }

    register("generateTests") {
        dependsOn("generateDAOs")
        doLast {
            val sourceDir = file(daoTestPackagePath)
            sourceDir.mkdirs()

            (1..generationSize).forEach { i ->
                val employeeDaoTestFile = File(sourceDir, "Employee${i}DaoTest.java")
                writeEmployeeDaoTestCode(employeeDaoTestFile, i)
            }
            println("Generated DAO test files in src/test/java/com/example/dao")
        }
    }

    register("generateEntities") {
        doLast {
            val sourceDir = file(entityPackagePath)
            sourceDir.mkdirs()
            (1..generationSize).forEach { i ->
                val empFile = File(sourceDir, "Employee$i.java")
                writeEmployeeCode(empFile, i)
                val deptFile = File(sourceDir, "Department$i.java")
                writeDepartmentCode(deptFile, i)
            }
            println("Generated entity files in src/main/java/com/example/entity")
        }
    }

    register("generateSqlFiles") {
        doLast {
            (1..generationSize).forEach { i ->
                val dir = file("$sqlFileDirPath/Employee${i}Dao")
                dir.mkdirs()
                val sqlFile = File(dir, "selectById.sql")
                writeSelectByIdSqlFile(sqlFile, i)
                val createScriptFile = File(dir, "create.script")
                writeCreateScriptFile(createScriptFile, i)
                val dropScriptFile = File(dir, "drop.script")
                writeDropScriptFile(dropScriptFile, i)
            }
            println("Generated SQL files in src/main/resources/META-INF/com/example/dao/EmployeeXxxDao")
        }
    }

    register<Delete>("removeGeneratedFiles") {
        delete(file(daoPackagePath), file(daoTestPackagePath), file(entityPackagePath), file(sqlFileDirPath))
    }
}

fun writeEmployeeDaoCode(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        package com.example.dao;
        
        import org.seasar.doma.Dao;
        import org.seasar.doma.Insert;
        import org.seasar.doma.Update;
        import org.seasar.doma.Delete;
        import org.seasar.doma.Script;
        import org.seasar.doma.Select;
        import org.seasar.doma.Sql;
        import com.example.entity.Employee$i;
        
        @Dao
        public interface Employee${i}Dao {
            @Insert
            int insert(Employee$i entity);
        
            @Update
            int update(Employee$i entity);
        
            @Delete
            int delete(Employee$i entity);
            
            @Select(aggregateStrategy = Employee${i}AggregateStrategy.class)
            Employee$i selectById(Long id);

            @Script
            void create();

            @Script
            void drop();
        }
        """.trimIndent(),
    )
}

fun writeEmployeeAggregateStrategyCode(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        package com.example.dao;
        
        import org.seasar.doma.AggregateStrategy;
        import org.seasar.doma.AssociationLinker;
        
        import java.util.function.BiConsumer;
        
        import com.example.entity.Department$i;
        import com.example.entity.Employee$i;
        
        @AggregateStrategy(root = Employee$i.class, tableAlias = "e")
        public interface Employee${i}AggregateStrategy {
        
          @AssociationLinker(propertyPath = "department", tableAlias = "d")
          BiConsumer<Employee$i, Department$i> department =
              (e, d) -> {
                e.department = d;
                d.employees.add(e);
              };
        }
        """.trimIndent(),
    )
}

fun writeEmployeeCode(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        package com.example.entity;
        
        import org.seasar.doma.Association;
        import org.seasar.doma.Entity;
        import org.seasar.doma.Id;
        import org.seasar.doma.GeneratedValue;
        import org.seasar.doma.GenerationType;
        import org.seasar.doma.Metamodel;
        import org.seasar.doma.Version;
        import com.example.domain.Name;
        import java.net.URL;
        import java.time.LocalDate;

        @Entity(metamodel = @Metamodel)
        public class Employee$i {
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
            @Version
            public Integer version;
            @Association
            public Employee$i manager;
            @Association
            public Department$i department;
        }
        """.trimIndent(),
    )
}

fun writeDepartmentCode(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        package com.example.entity;
        
        import java.util.ArrayList;
        import java.util.List;
        
        import org.seasar.doma.Association;
        import org.seasar.doma.Entity;
        import org.seasar.doma.Id;
        import org.seasar.doma.GeneratedValue;
        import org.seasar.doma.GenerationType;
        import org.seasar.doma.Metamodel;
        import org.seasar.doma.Version;
        import com.example.domain.Name;

        @Entity(metamodel = @Metamodel)
        public class Department$i {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            public Long id;
            public Name name;
            @Version
            public Integer version;
            @Association
            public List<Employee$i> employees = new ArrayList<>();
        }
        """.trimIndent(),
    )
}

fun writeSelectByIdSqlFile(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        SELECT /*%expand*/*
          FROM employee$i e
               INNER JOIN department$i d
                       ON e.department_id = d.id
         WHERE e.id = /*id*/0 
        """.trimIndent(),
    )
}

fun writeCreateScriptFile(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        CREATE TABLE department$i (
            id BIGINT NOT NULL AUTO_INCREMENT,
            name VARCHAR(255),
            version INTEGER NOT NULL DEFAULT 0,
            PRIMARY KEY (id)
        );
        
        CREATE TABLE employee$i (
            id BIGINT NOT NULL AUTO_INCREMENT,
            name VARCHAR(255),
            age INTEGER,
            birthday DATE,
            profile_url VARCHAR(2048),
            description1 VARCHAR(4000),
            description2 VARCHAR(4000),
            description3 VARCHAR(4000),
            description4 VARCHAR(4000),
            description5 VARCHAR(4000),
            description6 VARCHAR(4000),
            description7 VARCHAR(4000),
            description8 VARCHAR(4000),
            description9 VARCHAR(4000),
            description10 VARCHAR(4000),
            manager_id BIGINT,
            department_id BIGINT,
            version INTEGER NOT NULL DEFAULT 0,
            PRIMARY KEY (id)
        );
        
        ALTER TABLE employee$i ADD CONSTRAINT fk_employee${i}_manager$i
             FOREIGN KEY (manager_id) REFERENCES employee$i(id);
        
        ALTER TABLE employee$i ADD CONSTRAINT fk_employee${i}_department$i
             FOREIGN KEY (department_id) REFERENCES department$i(id);
        
        -- Department data
        INSERT INTO department$i (id, name, version) VALUES
        (1, 'Engineering', 0),
        (2, 'Sales', 0),
        (3, 'Human Resources', 0),
        (4, 'Administration', 0);
        
        -- Employee data
        INSERT INTO employee$i (id, name, age, birthday, profile_url, description1, description2, description3, description4, description5, description6, description7, description8, description9, description10, manager_id, department_id, version) VALUES
        (1, 'John Smith', 45, '1979-04-15', 'https://example.com/profiles/john.smith', 'Engineering Department Head with 10 years experience', 'Expert in Java, Python, and Go programming', 'Strong team management skills', 'Promotes agile development practices', 'Strategic technology planning', 'Focus on team member development', 'Technical consultation with clients', 'Evaluation and adoption of new technologies', 'Project quality management', 'Cross-department coordination', NULL, 1, 0),
        (2, 'Sarah Johnson', 42, '1982-08-22', 'https://example.com/profiles/sarah.johnson', 'Sales Department Head for 8 years', 'Contributed to customer satisfaction improvement', 'Team sales target achievement rate 120%', 'Expert in new business development', 'Strong proposal and presentation skills', 'Building long-term client relationships', 'Sales strategy planning and execution', 'Supporting team skill development', 'Market analysis and competitive research', 'Sales budget management', 1, 1, 0);

        -- RESET IDENTITY
        ALTER TABLE department$i ALTER COLUMN id RESTART WITH 100;
        ALTER TABLE employee$i ALTER COLUMN id RESTART WITH 100;
        """.trimIndent(),
    )
}

fun writeDropScriptFile(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        DROP TABLE IF EXISTS employee$i;
        DROP TABLE IF EXISTS department$i;
        """.trimIndent(),
    )
}

fun writeEmployeeDaoTestCode(
    file: File,
    i: Int,
) {
    file.writeText(
        """
        package com.example.dao;

        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.seasar.doma.jdbc.Config;
        import org.seasar.doma.jdbc.Naming;
        import org.seasar.doma.jdbc.SimpleConfig;
        import org.seasar.doma.slf4j.Slf4jJdbcLogger;
        
        import com.example.domain.Name;
        import com.example.entity.Employee$i;

        import static org.junit.jupiter.api.Assertions.*;

        class Employee${i}DaoTest {

          private final Config config =
              SimpleConfig.builder("jdbc:h2:mem:test$i;DB_CLOSE_DELAY=-1")
                  .naming(Naming.SNAKE_LOWER_CASE)
                  .jdbcLogger(new Slf4jJdbcLogger())
                  .build();

          private final Employee${i}Dao employeeDao = new Employee${i}DaoImpl(config);

          @BeforeEach
          void setup() {
            employeeDao.drop();
            employeeDao.create();
          }
        
          @Test
          void insert() {
            var employee = new Employee$i();
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
        """.trimIndent(),
    )
}
