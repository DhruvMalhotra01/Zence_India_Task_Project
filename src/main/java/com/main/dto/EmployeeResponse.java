package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;

    private String name;

    private String email;

    private String department;

    private String position;

    private Double salary;

    private LocalDate dateOfJoining;
}