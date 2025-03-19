package com.jp.testingapp.repository;

import com.jp.testingapp.AbstractionBaseTest;
import com.jp.testingapp.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest extends AbstractionBaseTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al@test.com")
                .build();
        employeeRepository.save(employee);
    }

    @DisplayName("JUnit test for EmployeeRepository save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup

        // when - action or behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - assertion or verification
        assertThat(savedEmployee.getId()).isNotNull();

    }

    @DisplayName("JUnit test for ")
    @Test
    void givenEmployeeList_whenFindAll_thenEmployeesList() {
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("jondoe@dot.com")
                .build();
        employeeRepository.save(employee2);
        // when - action or behavior that we are going to test
        List<Employee> all = employeeRepository.findAll();
        // then - assertion or verification
        assertThat(all).isNotNull();
        assertThat(all).hasSize(2);

    }

    @DisplayName("JUnit test for test find by id")
    @Test
    void givenEmployee_whenFindById_thenReturnEmployee() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        Employee employeeById = employeeRepository.findById(employee.getId()).orElse(null);
        // then - assertion or verification
        assertThat(employeeById).isNotNull();
    }

    @DisplayName("JUnit test for test to update employee by last name")
    @Test
    void givenAnEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        Employee employeeUpdated = employeeRepository.findById(employee.getId()).orElse(null);
        assert employeeUpdated != null;
        employeeUpdated.setEmail("aaaaa@gmail.com");
        employeeUpdated.setFirstName("John");
        Employee updatedEmployee = employeeRepository.save(employeeUpdated);

        // then - assertion or verification
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("aaaaa@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("John");

    }

    @DisplayName("JUnit test for delete employee")
    @Test
    void givenAnEmployee_whenDeleted_thenRemoveEmployee() {
        // given - precondition or setup

        // when - action or behavior that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - assertion or verification
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query")
    @Test
    void givenFirstName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        Employee byJPQL = employeeRepository.findByJPQL("jp", "al");

        // then - assertion or verification
        assertThat(byJPQL).isNotNull();
    }

    @DisplayName("JUnit test for custom query named params")
    @Test
    void givenFirstName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        String firstName = "jp";
        String lastName = "al";
        Employee byJPQL = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - assertion or verification
        assertThat(byJPQL).isNotNull();
    }

    @DisplayName("JUnit test for custom query named params")
    @Test
    void givenEmployee_whenFindByEmailWithNativeQuery_thenReturnEmployeeObject() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        Employee employeeByEmail = employeeRepository.findByEmailWithNativeQuery("@test.com");

        // then - assertion or verification
        assertThat(employeeByEmail).isNotNull();
    }

    @DisplayName("JUnit test for custom query named params")
    @Test
    void givenEmployee_whenFindByEmailWithNativeQueryNamed_thenReturnEmployeeObject() {
        // given - precondition or setup
        // when - action or behavior that we are going to test
        Employee employeeByEmail = employeeRepository.findByEmailWithNativeWithNamedQuery("@test.com");

        // then - assertion or verification
        assertThat(employeeByEmail).isNotNull();
    }


}