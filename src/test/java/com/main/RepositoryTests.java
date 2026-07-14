package com.main;

import com.main.entity.Employee;
import com.main.entity.Role;
import com.main.entity.User;
import com.main.repository.EmployeeRepository;
import com.main.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save Employee")
    void testSaveEmployee() {

        Employee employee = Employee.builder()
                .name("Om Patil")
                .email("om@gmail.com")
                .department("IT")
                .position("Java Developer")
                .salary(50000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee);
        assertNotNull(savedEmployee.getId());
        assertEquals("Om Patil", savedEmployee.getName());
    }

    @Test
    @DisplayName("Find Employee By Id")
    void testFindEmployeeById() {

        Employee employee = Employee.builder()
                .name("Rahul")
                .email("rahul@gmail.com")
                .department("HR")
                .position("HR Manager")
                .salary(60000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        Optional<Employee> result =
                employeeRepository.findById(savedEmployee.getId());

        assertTrue(result.isPresent());
        assertEquals("Rahul", result.get().getName());
    }

    @Test
    @DisplayName("Employee Exists By Email")
    void testExistsByEmail() {

        Employee employee = Employee.builder()
                .name("John")
                .email("john@gmail.com")
                .department("IT")
                .position("Developer")
                .salary(65000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        employeeRepository.save(employee);

        boolean exists =
                employeeRepository.existsByEmail("john@gmail.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Delete Employee")
    void testDeleteEmployee() {

        Employee employee = Employee.builder()
                .name("Alex")
                .email("alex@gmail.com")
                .department("Sales")
                .position("Executive")
                .salary(45000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        Employee savedEmployee =
                employeeRepository.save(employee);

        employeeRepository.delete(savedEmployee);

        Optional<Employee> result =
                employeeRepository.findById(savedEmployee.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Save User")
    void testSaveUser() {

        User user = User.builder()
                .username("admin")
                .email("admin@gmail.com")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("admin", savedUser.getUsername());
    }

    @Test
    @DisplayName("Find User By Username")
    void testFindByUsername() {

        User user = User.builder()
                .username("om")
                .email("om@gmail.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        Optional<User> result =
                userRepository.findByUsername("om");

        assertTrue(result.isPresent());
        assertEquals("om", result.get().getUsername());
    }

    @Test
    @DisplayName("User Exists By Email")
    void testUserExistsByEmail() {

        User user = User.builder()
                .username("test")
                .email("test@gmail.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        assertTrue(
                userRepository.existsByEmail("test@gmail.com")
        );
    }

}