package com.example.tests;

import com.example.TestApplication;
import com.example.config.RestAssuredSpec;
import com.example.config.WireMockConfig;
import com.example.testutils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseTest extends RestAssuredSpec {

    public String TOKEN;

    @Autowired
    protected TestUtils utils;

    protected RestAssuredSpec restAssuredSpec = new RestAssuredSpec();
    protected RequestSpecification specForEndpoint;

    @BeforeEach
    void setup() {
        WireMockConfig.startWireMockOk();
        TOKEN = utils.genToken();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        specForEndpoint = restAssuredSpec.forEndpoint();

    }
    @AfterEach
    void afterEach(){
        WireMockConfig.stopWireMock();
    }

    @AfterAll
    void cleanup() {
        WireMockConfig.stopWireMock();
    }
}
