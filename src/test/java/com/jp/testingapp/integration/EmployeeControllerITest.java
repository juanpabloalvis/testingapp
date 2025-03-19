package com.jp.testingapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.testingapp.AbstractionBaseTest;
import com.jp.testingapp.model.Employee;
import com.jp.testingapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// @Testcontainers //We don't need, because we start the container manually
public class EmployeeControllerITest extends AbstractionBaseTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @DisplayName("Test to check mysql testcontainer configuratioj ")
    @Test
    void given_when_then() {
        System.out.println("mysql container");
    }

    @DisplayName("Integration test for create employee")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenCreateEmployee() throws Exception {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al1@test.com")
                .build();

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
                .andExpect(jsonPath("$.email", is(employee1.getEmail())))
                .andDo(print());

    }

    @DisplayName("Integration test for get all employees")
    @Test
    void givenEmployeeObject_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al1@test.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al1@test.com")
                .build();
        List<Employee> employeeList = List.of(employee1, employee2);
        employeeRepository.saveAll(employeeList);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));

        // then - assertion or verification
        response.andExpect(status()
                        .isOk())
                .andDo(print())
                // dollar is the root object
                .andExpect(jsonPath("$.size()", is(employeeList.size())))
                .andExpect(jsonPath("$[0].id", is(employee1.getId().intValue())))
                .andExpect(jsonPath("$[0].lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee1.getEmail())))

                .andExpect(jsonPath("$[1].id", is(employee2.getId().intValue())))
                .andExpect(jsonPath("$[1].lastName", is(employee2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(employee2.getEmail())))
                .andDo(print());

    }

    @DisplayName("Integration test for create employee")
    @Test
    void givenEmployee_whenFindById_thenCreateEmployee() throws Exception {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al1@test.com")
                .build();
        employeeRepository.save(employee1);
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Custom-Header", "ThisIsMyHeader")
                .content(objectMapper.writeValueAsString(employee1)));

        // then - assertion or verification
        response.andExpect(status()
                        .isOk())
                // dollar is the root object
                .andExpect(jsonPath("$.id", is(employee1.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())))
                .andDo(print());

    }

    @DisplayName("Integration test for update an employee positive scenario")
    @Test
    void givenEmployeeObject_whenUpdateAnEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("jp12")
                .lastName("al34")
                .email("jp.al156@test.com")
                .build();
        Employee toUpdateObject = Employee.builder()
                .firstName("updatedName")
                .lastName("updateLast")
                .email("jp.al156@test.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdateObject))
        );

        // then - assertion or verification
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(toUpdateObject.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(toUpdateObject.getLastName())))
                .andExpect(jsonPath("$.email", is(toUpdateObject.getEmail())))
                .andDo(print());

    }

    @DisplayName("Integration test for update an employee by id negative scenario")
    @Test
    void givenEmployeeObject_whenUpdateAnEmployee_thenReturnUpdatedEmployeeNegative() throws Exception {
        // given - precondition or setup
        // no existent object because it was not persisted
        long employeeId = 1L;
        Employee employee1 = Employee.builder()
                .firstName("jp")
                .lastName("al")
                .email("jp.al1@test.com")
                .build();


        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
        );

        // then - assertion or verification
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("Integration test for Controller that delete")
    @Test
    void givenAnEmployee_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("jp12")
                .lastName("al34")
                .email("jp.al156@test.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee1.getId())
                .contentType(MediaType.APPLICATION_JSON));
        // then - assertion or verification
        response.andExpect(status().isOk())
                .andDo(print());

    }

}
