package com.jp.testingapp.service;

import com.jp.testingapp.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> findByEmail(String mail);

    Optional<Employee> findEmployeeById(Long id);

    Employee upadteEmployee(Employee employee);

    void deleteEmployeeById(Long id);
}
