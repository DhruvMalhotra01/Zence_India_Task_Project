package com.main.api;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class EmployeeApiTest extends BaseApiTest {

    @Test
    void createEmployee_shouldReturn201() {

        String email = "restassured" + System.currentTimeMillis() + "@test.com";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                {
                    "name": "Dhruv REST Assured",
                    "email": "%s",
                    "department": "IT",
                    "position": "QA Engineer",
                    "salary": 50000,
                    "dateOfJoining": "2026-09-22"
                }
            """.formatted(email))
                .when()
                .post("/api/employees")
                .then()
                .statusCode(201);
//                .body("name", equalTo("Dhruv REST Assured"))
//                .body("email", equalTo(email))
//                .body("department", equalTo("IT"))
//                .body("position", equalTo("QA Engineer"))
//                .body("salary", equalTo(50000.0F))
//                .body("id", notNullValue());
    }
    @Test
    void getAllEmployees_shouldReturn200() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }
    @Test
    void getEmployees_shouldSupportPagination() {

        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .queryParam("sortBy", "id")
                .queryParam("sortDir", "asc")
                .when()
                .get("/api/employees")
                .then()
                .statusCode(200)
                .body("number", equalTo(0))
                .body("size", equalTo(5));
    }
    @Test

    void getEmployeeById_shouldReturn200() {

        String email = "get" + System.currentTimeMillis() + "@test.com";

        Long employeeId =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .body("""
                        {
                            "name": "GET Test Employee",
                            "email": "%s",
                            "department": "IT",
                            "position": "QA Engineer",
                            "salary": 50000,
                            "dateOfJoining": "2026-09-22"
                        }
                    """.formatted(email))
                        .when()
                        .post("/api/employees")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees/{id}", employeeId)
                .then()
                .statusCode(200)
                .body("id", equalTo(employeeId.intValue()));
    }
    @Test
    void updateEmployee_shouldReturn200() {

        // 1. Create employee
        String email = "update" + System.currentTimeMillis() + "@test.com";

        Long employeeId =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .body("""
                        {
                            "name": "Update Test Employee",
                            "email": "%s",
                            "department": "IT",
                            "position": "QA Engineer",
                            "salary": 50000,
                            "dateOfJoining": "2026-09-22"
                        }
                    """.formatted(email))
                        .when()
                        .post("/api/employees")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");

        // 2. Update the same employee
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                {
                    "name": "Dhruv Updated",
                    "email": "%s",
                    "department": "Testing",
                    "position": "Automation Tester",
                    "salary": 60000,
                    "dateOfJoining": "2026-09-22"
                }
            """.formatted(email))
                .when()
                .put("/api/employees/{id}", employeeId)
                .then()
                .statusCode(200)
                .body("id", equalTo(employeeId.intValue()))
                .body("name", equalTo("Dhruv Updated"))
                .body("email", equalTo(email))
                .body("department", equalTo("Testing"))
                .body("position", equalTo("Automation Tester"))
                .body("salary", equalTo(60000.0F));
    }
    @Test

    void deleteEmployee_shouldReturn200() {

        // 1. Create an employee
        String email = "delete" + System.currentTimeMillis() + "@test.com";

        Long employeeId =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .body("""
                        {
                            "name": "Delete Test Employee",
                            "email": "%s",
                            "department": "IT",
                            "position": "QA Engineer",
                            "salary": 50000,
                            "dateOfJoining": "2026-09-22"
                        }
                    """.formatted(email))
                        .when()
                        .post("/api/employees")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");

        // 2. Delete the newly created employee
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/employees/{id}", employeeId)
                .then()
                .statusCode(200)
                .body(equalTo("Employee deleted successfully."));
    }
    @Test
    void getDeletedEmployee_shouldReturn404() {

        Long employeeId = 1L;

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees/{id}", employeeId)
                .then()
                .statusCode(404)
                .body("error", equalTo("Not Found"));
    }

    @Test
    void createEmployee_invalidEmail_shouldReturn400() {

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
            {
                "name": "Dhruv",
                "email": "abc",
                "department": "IT",
                "position": "QA Engineer",
                "salary": 50000,
                "dateOfJoining": "2026-09-22"
            }
        """)
                .when()
                .post("/api/employees")
                .then()
                .statusCode(400)
                .body("error", equalTo("Validation Failed"))
                .body("messages.email", equalTo("Invalid email format"));
    }
    @Test
    void createEmployee_negativeSalary_shouldReturn400() {

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
            {
                "name": "Dhruv",
                "email": "salary@test.com",
                "department": "IT",
                "position": "QA Engineer",
                "salary": -5000,
                "dateOfJoining": "2026-09-22"
            }
        """)
                .when()
                .post("/api/employees")
                .then()
                .statusCode(400)
                .body("messages.salary",
                        equalTo("Salary must be greater than 0"));
    }
    @Test
    void createEmployee_missingName_shouldReturn400() {

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
            {
                "email": "noname@test.com",
                "department": "IT",
                "position": "QA Engineer",
                "salary": 50000,
                "dateOfJoining": "2026-09-22"
            }
        """)
                .when()
                .post("/api/employees")
                .then()
                .statusCode(400)
                .body("messages.name", equalTo("Name is required"));
    }
    @Test
    void login_wrongPassword_shouldReturn401() {

        given()
                .contentType("application/json")
                .body("""
            {
                "username": "dhruv_test",
                "password": "WrongPassword"
            }
        """)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }
    @Test
    void getEmployees_withoutToken_shouldBeUnauthorized() {

        given()
                .when()
                .get("/api/employees")
                .then()
                .statusCode(403);
    }
    @Test
    void getEmployee_nonExistingId_shouldReturn404() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/employees/{id}", 999999L)
                .then()
                .statusCode(404)
                .body("error", equalTo("Not Found"));
    }
}