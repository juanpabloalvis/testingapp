package com.jp.testingapp.controller;

import com.jp.testingapp.model.Employee;
import com.jp.testingapp.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Custom-Response-Header", "ThisIsMyHeader")
                .body(employeeService.createEmployee(employee));
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("id") long id) {

        return employeeService.findEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {

        return employeeService.findEmployeeById(id)
                .map(existentEmployee -> {
                    existentEmployee.setFirstName(employee.getFirstName());
                    existentEmployee.setLastName(employee.getLastName());
                    existentEmployee.setEmail(employee.getEmail());
                    Employee updatedEmployee = employeeService.upadteEmployee(existentEmployee);
                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {

        return employeeService.findEmployeeById(id)
                .map(existentEmployee -> {
                    employeeService.deleteEmployeeById(id);
                    return new ResponseEntity<String>("Employee delete successfully!", HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
