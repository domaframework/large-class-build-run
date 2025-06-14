plugins {
    id("java")
    id("application")
    alias(libs.plugins.doma.compile)
    alias(libs.plugins.spotless)
}

group = "com.example"
version = "1.0-SNAPSHOT"

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

var generationSize = 200

application {
    mainClass.set("com.example.Main")
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
        dependsOn("generateDAOs", "generateEntities", "generateSqlFiles")
    }

    var daoPackagePath = "src/main/java/com/example/dao"
    var entityPackagePath = "src/main/java/com/example/entity"
    var sqlFileDirPath = "src/main/resources/META-INF/com/example/dao"

    register("generateDAOs") {
        dependsOn("generateEntities")
        doLast {
            val sourceDir = file(daoPackagePath)
            sourceDir.mkdirs()

            (1..generationSize).forEach { i ->
                val employeeDaoFile = File(sourceDir, "Employee${i}Dao.java")
                writeEmployeeDaoCode(employeeDaoFile, i)
            }
            println("Generated DAO files in src/main/java/com/example/dao")
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
                sqlFile.writeText("select /*%expand*/* from employee$i where id = /*id*/0\n")
            }
            println("Generated SQL files in src/main/resources/META-INF/com/example/dao/EmployeeXxxDao")
        }
    }

    register<Delete>("removeGeneratedFiles") {
        delete(file(entityPackagePath), file(daoPackagePath), file(sqlFileDirPath))
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
            
            @Select
            Employee$i selectById(Long id);
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
            public List<Employee$i> employees;
        }
        """.trimIndent(),
    )
}

spotless {
    java {
        googleJavaFormat(libs.versions.googleJavaFormat.get())
        target("src/*/java/**/*.java")
        targetExclude("**/generated/**")
    }
    kotlin {
        target("*.gradle.kts")
        ktlint()
    }
}
