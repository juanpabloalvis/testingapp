package com.jp.testingapp.service.impl;

import com.jp.testingapp.exception.ResourceAlreadyExists;
import com.jp.testingapp.model.Employee;
import com.jp.testingapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    // auto-inject employeeRepository, but we need to add @ExtendWith(MockitoExtension.class)
    @InjectMocks
    private EmployeeServiceImpl employeeService;


    private Employee employee;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder().id(1L).firstName("jp").lastName("al").email("jp.al1@test.com").build();
        employee2 = Employee.builder().id(2L).firstName("jp").lastName("al").email("jp.al2@test.com").build();
    }

    @DisplayName("JUnit test for test service find all employees")
    @Test
    void givenEmployeeObject_whenGetAllEmployees_thenFindAllEmployees() {

        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));
        // when - action or behavior that we are going to test
        List<Employee> employees = employeeService.getAllEmployees();
        // then - assertion or verification
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(2);
    }

    @DisplayName("JUnit test for test service find emptyList")
    @Test
    void givenEmployeeObject_whenGetAllEmployees_thenFindEmptyList() {

        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // when - action or behavior that we are going to test
        List<Employee> employees = employeeService.getAllEmployees();
        // then - assertion or verification
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for test service for save employee")
    @Test
    void givenNewEmployee_whenSave_Employee_thenReturnTheEmployee() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        // when - action or behavior that we are going to test
        Employee employeeByEmail = employeeService.createEmployee(employee);
        // then - assertion or verification
        assertThat(employeeByEmail).isNotNull();
    }

    @DisplayName("JUnit test for test service find by email")
    @Test
    void givenEmployeeObject_whenFindByEmail_thenReturnTheEmployee2() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        // when - action or behavior that we are going to test
        Employee employeeByEmail = employeeService.findByEmail(employee.getEmail()).orElse(null);
        // then - assertion or verification
        assertThat(employeeByEmail).isNotNull();
    }

    @DisplayName("JUnit test for exception when an employee exists")
    @Test
    void givenEmployeeAnExistentEmail_wheCreateEmployeeEmployee_thenThrowsException() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        // when - action or behavior that we are going to test
        assertThrows(ResourceAlreadyExists.class, () -> {
            employeeService.createEmployee(employee);
        });
        // then - assertion or verification
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for find By Id")
    @Test
    void givenEmployeeObject_wheCreateEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behavior that we are going to test
        Employee saveEmployee = employeeService.findEmployeeById(employee.getId()).get();

        // then - assertion or verification
        assertThat(saveEmployee).isNotNull();
    }


    @DisplayName("JUnit test UpdatedEmployee method")
    @Test
    void givenEmployeeObject_wheUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behavior that we are going to test
        String newName = "Juan P";
        String newLastName = "AL";
        employee.setFirstName(newName);
        employee.setLastName(newLastName);
        Employee updatedEmployee = employeeService.upadteEmployee(employee);

        // then - assertion or verification
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo(newName);
        assertThat(updatedEmployee.getLastName()).isEqualTo(newLastName);

    }
    @DisplayName("JUnit test DeleteEmployee method")
    @Test
    void givenEmployeeObject_wheDeleteEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        long employeeId = 1L;
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or behavior that we are going to test
        employeeService.deleteEmployeeById(employeeId);

        // then - assertion or verification
        verify(employeeRepository, times(1)).deleteById(employeeId);


    }
}