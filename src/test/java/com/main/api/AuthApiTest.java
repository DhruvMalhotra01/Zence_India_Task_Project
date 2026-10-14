package com.main.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class AuthApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }

    @Test
    void login_shouldReturnJwtToken() {

        given()
                .contentType("application/json")
                .body("""
                {
                    "username": "dhruv_test",
                    "password": "Test@123"
                }
            """)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("tokenType", equalTo("Bearer"))
                .body("username", equalTo("dhruv_test"))
                .body("role", equalTo("ROLE_USER"));
    }
}