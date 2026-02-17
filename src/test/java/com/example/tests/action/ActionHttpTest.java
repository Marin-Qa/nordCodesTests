package com.example.tests.action;

import com.example.config.WireMockConfig;
import com.example.endpoints.service.EndpointsService;
import com.example.param.ParamName;
import com.example.param.action.ActionName;
import com.example.tests.BaseTest;
import com.example.testutils.AllureUtils;
import com.example.testutils.TestUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;

@Epic("API")
@DisplayName("Action HTTP")
@Feature("Action")
@Story("HTTP")
public class ActionHttpTest extends BaseTest {

    private String dynamicToken;

    @Autowired
    AllureUtils allureUtils;

    @Autowired
    TestUtils testUtils;

    @BeforeEach
    void setUp() {
        dynamicToken = utils.genToken();

        Allure.step("Шаг 1. Делаем запрос action для успешного ответа", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.LOGIN)
                    .header(ParamName.TOKEN, dynamicToken)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, TOKEN);

            response.then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("result", equalTo("OK"));
        });
    }
    @Test
    @DisplayName("Проверка action на HTTP ответ от внешнего сервиса 408")
    @Description("Проверяем ответ 408 на action.  От внешнего клиента видимо сервис на 408 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testActionFailed_whenTimeoutHttp(){
        WireMockConfig.stubDoAction(408, "");
        // От внешнего клиента видимо сервис на 408 отвечает 500. Поэтому проверяем 500
        executeRequestForAction(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Test
    @DisplayName("Проверка action на HTTP ответ от внешнего сервиса 500")
    @Description("Проверяем ответ 500 на action")
    @Owner("Marin")
    void testActionFailed_whenInternalServerErrorHttp(){
        WireMockConfig.stubDoAction(500, "");
        executeRequestForAction(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
    @Test
    @DisplayName("Проверка action на HTTP ответ от внешнего сервиса 400")
    @Description("Проверяем ответ 400 на action. От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testActionFailed_whenBadRequestHttp() {
        WireMockConfig.stubDoAction(400, "");
        // От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500
        executeRequestForAction(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Test
    @DisplayName("Проверка action на HTTP ответ от внешнего сервиса 401")
    @Description("Проверяем ответ 401 на action. От внешнего клиента видимо сервис на 401 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testActionFailed_whenUnauthorizedHttp() {
        WireMockConfig.stubDoAction(401, "");
        // От внешнего клиента видимо сервис на 401 отвечает 500. Поэтому проверяем 500
        executeRequestForAction(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Test
    @DisplayName("Проверка action на HTTP ответ от внешнего сервиса 403")
    @Description("Проверяем ответ 403 на action. От внешнего клиента видимо сервис на 400 отвечает 500. Поэтому проверяем 500")
    @Owner("Marin")
    void testActionFailed_whenForbiddenHttp() {
        WireMockConfig.stubDoAction(403, "");
        // От внешнего клиента видимо сервис на 403 отвечает 500. Поэтому проверяем 500
        executeRequestForAction(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private void executeRequestForAction(int statusCode, String message) {

        Allure.step("Шаг 2. Выполняем запрос action", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.ACTION)
                    .header(ParamName.TOKEN, dynamicToken)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response);

            response.then()
                    .statusCode(statusCode)
                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo(message));
        });
    }
}
