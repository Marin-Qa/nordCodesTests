package com.example.tests.logout;

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
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static org.hamcrest.Matchers.equalTo;

@Epic("API")
@DisplayName("Logout")
@Feature("Logout")
@Story("API")
public class LogoutTest extends BaseTest {

    @Autowired
    AllureUtils allureUtils;

    @BeforeEach
    void setIp(TestInfo info){
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
        }
    }

    @Test
    @DisplayName("Проверка logout успех")
    @Description("Проверяем успешный ответ на logout")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testActionSuccess(){

        Allure.step("Шаг 3. Делаем запрос logout для успешного ответа", () -> {
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
    }
}
