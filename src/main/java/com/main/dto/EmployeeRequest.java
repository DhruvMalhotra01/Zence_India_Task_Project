package com.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EmployeeRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Department is required")
        String department,

        @NotBlank(message = "Position is required")
        String position,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than 0")
        Double salary,

        @NotNull(message = "Date of joining is required")
        LocalDate dateOfJoining

) {
}