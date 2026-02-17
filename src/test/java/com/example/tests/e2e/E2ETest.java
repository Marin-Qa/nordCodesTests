package com.example.tests.e2e;

import com.example.endpoints.service.EndpointsService;
import com.example.param.ParamName;
import com.example.param.action.ActionName;
import com.example.tests.BaseTest;
import com.example.testutils.AllureUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("API")
@DisplayName("E2E")
@Story("E2E")
public class E2ETest extends BaseTest {

    @Autowired
    AllureUtils allureUtils;

    @Test
    @DisplayName("Полный успешный путь")
    @Description("Проверяем успешный полный путь")
    @Owner("Marin")
    @Severity(SeverityLevel.BLOCKER)
    void testLoginSuccess_E2E() {
        Allure.step("Шаг 1. Выполняем login", () -> {

            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.LOGIN)
                    .header(ParamName.TOKEN, TOKEN)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, TOKEN);

            response.then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("result", equalTo("OK"));
        });

        verify(postRequestedFor(urlEqualTo("/auth"))
                .withRequestBody(containing(TOKEN)));

        Allure.step("Шаг 2. Выполняем action", () -> {
                   Response response =  RestAssured.given()
                            .spec(specForEndpoint)
                            .header(ParamName.ACTION, ActionName.ACTION)
                            .header(ParamName.TOKEN, TOKEN)
                            .post(EndpointsService.ENDPOINT);
                   allureUtils.forAllure(response, TOKEN);

            response.then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("result", equalTo("OK"));

                });

        verify(postRequestedFor(urlEqualTo("/doAction"))
                .withRequestBody(containing(TOKEN)));

       Allure.step("Шаг 3. Выполняем logout", () -> {
           Response response = RestAssured.given()
                   .spec(specForEndpoint)
                   .header(ParamName.ACTION, ActionName.LOGOUT)
                   .header(ParamName.TOKEN, TOKEN)
                   .post(EndpointsService.ENDPOINT);

           allureUtils.forAllure(response, TOKEN);

           response.then()
                   .statusCode(HttpStatus.SC_OK)
                   .body("result", equalTo("OK"));
       });


        Allure.step("Шаг 3.1. Проверка что выполнился logout", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.ACTION)
                    .header(ParamName.TOKEN, TOKEN)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, TOKEN);

            response.then()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("result", equalTo("ERROR"));
        });
    }
}
