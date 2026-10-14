package com.main.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;

public class BaseApiTest {

    protected static String token;

    @BeforeAll
    static void setup() {

        RestAssured.baseURI = "http://localhost:8081";

        Response response =
                given()
                        .contentType("application/json")
                        .body("""
                        {
                            "username": "dhruv_test",
                            "password": "Test@123"
                        }
                    """)
                        .when()
                        .post("/api/auth/login");

        token = response.jsonPath().getString("token");

}
}

























