package com.jp.testingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.testingapp.AbstractionBaseTest;
import com.jp.testingapp.model.Employee;
import com.jp.testingapp.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest extends AbstractionBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private List<Employee> employeeList;


    @BeforeEach
    void setUp() {
        employee1 = Employee.builder().id(1L).firstName("jp").lastName("al").email("jp.al1@test.com").build();
        employee2 = Employee.builder().id(2L).firstName("jp2").lastName("al2").email("jp2.al1@test.com").build();
        employee3 = Employee.builder().id(3L).firstName("jp3").lastName("al3").email("jp3.al1@test.com").build();
        employeeList = List.of(employee1, employee2, employee3);
    }

    @DisplayName("JUnit test for Controller create employee")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        given(employeeService.createEmployee(any(Employee.class)))
                // argument 0 is the parameter 0, in this case the body
                .willAnswer((invocation -> invocation.getArgument(0)));
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Custom-Header", "ThisIsMyHeader")
                .content(objectMapper.writeValueAsString(employee1)));

        // then - assertion or verification
        response.andExpect(status()
                        .isCreated())
                .andExpect(header().string("Custom-Response-Header", "ThisIsMyHeader"))
                // dollar is the root object
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    @DisplayName("JUnit test for Controller to get all employees")
    @Test
    void givenEmployeeList_whenGetAllEmployee_thenReturnTheEmployeeList() throws Exception {
        // given - precondition or setup
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));

        // then - assertion or verification
        response.andExpect(status()
                        .is2xxSuccessful())
                .andDo(print())
                // dollar is the root object
                .andExpect(jsonPath("$.size()", is(employeeList.size())))
                .andExpect(jsonPath("$[0].id", is(employee1.getId().intValue())))
                .andExpect(jsonPath("$[0].lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee1.getEmail())));
    }


    @DisplayName("JUnit test for Controller find employee by id positive scenario")
    @Test
    void givenEmployeeObject_whenFindEmployeeById_thenReturnAnEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.of(employee1));
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - assertion or verification
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(employee1.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }


    @DisplayName("JUnit test for Controller find employee by id negative scenario")
    @Test
    void givenEmployeeObject_whenFindEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.empty());
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - assertion or verification
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit test for Controller update an employee positive scenario")
    @Test
    void givenEmployeeObject_whenUpdateAnEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.of(employee1));

        given(employeeService.upadteEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee2))
        );

        // then - assertion or verification
        response.andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("JUnit test for Controller update an employee by id negative scenario")
    @Test
    void givenEmployeeObject_whenUpdateAnEmployee_thenReturnUpdatedEmployeeNegative() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.empty());
        given(employeeService.upadteEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
        );

        // then - assertion or verification
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("JUnit test for Controller that delete")
    @Test
    void given_when_then() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.of(employee1));
        willDoNothing().given(employeeService).deleteEmployeeById(employeeId);
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));
        // then - assertion or verification
        response.andExpect(status().isOk())
                .andDo(print());

    }
}