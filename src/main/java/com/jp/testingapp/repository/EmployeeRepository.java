package com.jp.testingapp.repository;

import com.jp.testingapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByLastName(String lastName);

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE e.firstName =:firstName AND e.lastName =:lastName")
    Employee findByJPQLNamedParams(String firstName, String lastName);

    @Query(value = "SELECT * FROM employees e WHERE e.email like %?1%", nativeQuery = true)
    Employee findByEmailWithNativeQuery(@Param("email") String email);

    @Query(value = "SELECT * FROM employees e WHERE e.email LIKE %:email%", nativeQuery = true)
    Employee findByEmailWithNativeWithNamedQuery(@Param("email") String email);
}
