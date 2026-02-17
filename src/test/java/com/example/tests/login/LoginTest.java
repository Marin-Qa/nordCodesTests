package com.example.tests.login;

import com.example.endpoints.service.EndpointsService;
import com.example.param.ParamName;
import com.example.param.action.ActionName;
import com.example.tests.BaseTest;
import com.example.testutils.AllureUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static org.hamcrest.Matchers.equalTo;

@Epic("API")
@DisplayName("Login")
@Feature("Login")
@Story("API")
public class LoginTest extends BaseTest {

    @Autowired
    AllureUtils allureUtils;

    @Test
    @DisplayName("Проверка login")
    @Description("Проверяем успешный ответ на login")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testLoginSuccess(){
        Allure.step("Шаг 1. Делаем запрос login для успешного ответа", () -> {
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
    }

    @Test
    @DisplayName("Проверка login")
    @Description("Проверяем ответ на login c дублирующим токеном")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testLoginDuplicate(){
        Allure.step("Шаг 1. Делаем запрос login для успешного ответа", () -> {
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

        Allure.step("Шаг 2. Делаем повторной запрос login для дубликата токена", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.LOGIN)
                    .header(ParamName.TOKEN, TOKEN)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, TOKEN);

            response.then()
                    .statusCode(HttpStatus.SC_CONFLICT)
                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo(String.format("Token '%s' already exists", TOKEN)));
        });
    }

    @ParameterizedTest
    @DisplayName("Проверка login")
    @Description("Проверяем неуспешный ответ на login")
    @Owner("Marin")
    @MethodSource("provideTestLoginFailed")
    void testLoginFailed(String token){

        Allure.step("Шаг 1. Делаем запрос login для успешного ответа", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.LOGIN)
                    .header(ParamName.TOKEN, token)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, token);

            response.then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo("token: должно соответствовать \"^[0-9A-F]{32}$\""));
        });
    }

    private static Stream<Arguments> provideTestLoginFailed() {
        return Stream.of(
                Arguments.of("12345678901234567890123456789ABCDEF"),
                Arguments.of("a1b2c3d4e5f67890123456789abcdef0"),
                Arguments.of("12345-6789-0123-4567-89ABCDEF012345")
        );
    }

}
