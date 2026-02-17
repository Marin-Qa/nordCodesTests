package com.example.config;

import com.example.config.keys.ApiKeys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpec {

    private String apiKey;

    public RequestSpecification forEndpoint() {

        apiKey = ApiKeys.API_KEY;
        return  RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", apiKey);
    }
}
