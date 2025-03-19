package com.jp.testingapp;

import com.jp.testingapp.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EntityAppTest extends AbstractionBaseTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for @DataJpaTest annotation and its auto-configurations beans")
    @Test
    void contextLoads() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(employeeRepository).isNotNull();
    }

    @DisplayName("JUnit test for ")
    @Test
    void givenSubject_when_then() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        // then - assertion or verification
    }

}
