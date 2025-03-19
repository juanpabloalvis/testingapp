package com.jp.testingapp.service.impl;

import com.jp.testingapp.exception.ResourceAlreadyExists;
import com.jp.testingapp.exception.ResourceNotFound;
import com.jp.testingapp.model.Employee;
import com.jp.testingapp.repository.EmployeeRepository;
import com.jp.testingapp.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {

        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new ResourceAlreadyExists(String.format("Employee with given email [%s] already exists", employee.getEmail()));
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee upadteEmployee(Employee employee) {
        Optional<Employee> byId = employeeRepository.findById(employee.getId());
        if (byId.isPresent()) {
            return employeeRepository.save(employee);
        }
        throw new ResourceNotFound(String.format("Employee with given id [%d] does not exist", employee.getId()));

    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(employee -> employeeRepository.deleteById(employee.getId()), () -> {
                    throw new ResourceNotFound(String.format("Employee with given id [%d] does not exist", id));
                });
    }
}
