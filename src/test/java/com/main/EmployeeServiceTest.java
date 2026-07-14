package com.main;

import com.main.dto.EmployeeRequest;
import com.main.dto.EmployeeResponse;
import com.main.entity.Employee;
import com.main.exception.ResourceNotFoundException;
import com.main.repository.EmployeeRepository;
import com.main.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequest employeeRequest;

    @BeforeEach
    void setUp() {

        employee = Employee.builder()
                .id(1L)
                .name("Om Patil")
                .email("om@gmail.com")
                .department("IT")
                .position("Java Developer")
                .salary(50000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        employeeRequest = new EmployeeRequest(
                "Om Patil",
                "om@gmail.com",
                "IT",
                "Java Developer",
                50000.0,
                LocalDate.now()
        );
    }

    @Test
    void testCreateEmployee() {

        when(employeeRepository.existsByEmail(employeeRequest.email())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse response = employeeService.createEmployee(employeeRequest);

        assertNotNull(response);
        assertEquals("Om Patil", response.getName());
        assertEquals("om@gmail.com", response.getEmail());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_EmailAlreadyExists() {

        when(employeeRepository.existsByEmail(employeeRequest.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.createEmployee(employeeRequest));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Om Patil", response.getName());
    }

    @Test
    void testGetEmployeeById_NotFound() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void testGetAllEmployees() {

        Page<Employee> page =
                new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<EmployeeResponse> response =
                employeeService.getAllEmployees(0,5,"id","asc");

        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateEmployee() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse response =
                employeeService.updateEmployee(1L, employeeRequest);

        assertNotNull(response);
        assertEquals("Om Patil", response.getName());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void testDeleteEmployee_NotFound() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));
    }

}