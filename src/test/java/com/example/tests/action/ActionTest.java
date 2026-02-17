package com.example.tests.action;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static org.hamcrest.Matchers.equalTo;
@Epic("API")
@DisplayName("Action")
@Feature("Action")
@Story("API")
public class ActionTest extends BaseTest {

    @Autowired
    AllureUtils allureUtils;

    @BeforeEach
    void setUp(TestInfo info) {
        if (!info.getTags().contains("skipSetup")){
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
        }
    }

    @Test
    @DisplayName("Проверка action успех")
    @Description("Проверяем успешный ответ на action")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testActionSuccess(){

        Allure.step("Шаг 2. Делаем запрос action для успешного ответа", () -> {
            Response response = RestAssured.given()
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
    }

    @ParameterizedTest
    @Tag("skipSetup")
    @DisplayName("Проверка action без токена")
    @Description("Проверяем ответ от action, если токен не найден")
    @Owner("Marin")
    @MethodSource("provideTestActionFailed")
    void testActionFailed(String token){
        Allure.step("Шаг 1. Делаем запрос action для Forbidden", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.ACTION)
                    .header(ParamName.TOKEN, token)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, token);

            response.then()
                    .log().body()
                    .statusCode(HttpStatus.SC_FORBIDDEN)

                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo(String.format("Token '%s' not found", token)));
        });
    }

    private static Stream<Arguments> provideTestActionFailed() {
        return Stream.of(
                Arguments.of("A08A37101F814F468B2F6A89CDA48CBB"),
                Arguments.of("F0FD70CD81254C8B9D69A36B3DEA712C"),
                Arguments.of("E561F3FD0237446FB7B300E08E94D400")
        );
    }
}
