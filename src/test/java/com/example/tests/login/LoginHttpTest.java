package com.example.tests.login;

import com.example.config.WireMockConfig;
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
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
@Epic("API")
@DisplayName("Login HTTP")
@Feature("Login")
@Story("HTTP")
public class LoginHttpTest extends BaseTest {

    @Autowired
    AllureUtils allureUtils;

    @Test
    @DisplayName("Проверка login на HTTP ответ от внешнего сервиса 408")
    @Description("Проверяем ответ 408 на login.  От внешнего клиента видимо сервис на 408 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testLoginFailed_whenTimeoutHttp() {
        WireMockConfig.stubAuth(408, "");
        // От внешнего клиента видимо сервис на 408 отвечает 500. Поэтому проверяем 500
        executeRequestForLogin(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
    @Test
    @DisplayName("Проверка login на HTTP ответ от внешнего сервиса 500")
    @Description("Проверяем ответ 500 на login")
    @Owner("Marin")
    void testLoginFailed_whenInternalServerErrorHttp() {
        WireMockConfig.stubAuth(500, "");
        executeRequestForLogin(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
    @Test
    @DisplayName("Проверка login на HTTP ответ от внешнего сервиса 400")
    @Description("Проверяем ответ 400 на login. От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testLoginFailed_whenBadRequestHttp() {
        WireMockConfig.stubAuth(400, "");
        // От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500
        executeRequestForLogin(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Test
    @DisplayName("Проверка login на HTTP ответ от внешнего сервиса 401")
    @Description("Проверяем ответ 401 на login. От внешнего клиента видимо сервис на 401 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testLoginFailed_whenUnauthorizedHttp() {
        WireMockConfig.stubAuth(401, "");
        // От внешнего клиента видимо сервис на 401 отвечает 500. Поэтому проверяем 500
        executeRequestForLogin(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Test
    @DisplayName("Проверка login на HTTP ответ от внешнего сервиса 403")
    @Description("Проверяем ответ 403 на login. От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testLoginFailed_whenForbiddenHttp() {
        WireMockConfig.stubAuth(403, "");
        // От внешнего клиента видимо сервис на 403 отвечает 500. Поэтому проверяем 500
        executeRequestForLogin(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private void executeRequestForLogin(int statusCode, String message){

        Allure.step("Шаг 1. Выполняем запрос login", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.LOGIN)
                    .header(ParamName.TOKEN, TOKEN)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response);

            response.then()
                    .statusCode(statusCode)
                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo(message));
        });
    }
}
