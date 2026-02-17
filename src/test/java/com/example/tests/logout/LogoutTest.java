package com.example.tests.logout;

import com.example.endpoints.service.EndpointsService;
import com.example.param.ParamName;
import com.example.param.action.ActionName;
import com.example.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.equalTo;

@Epic("API")
@DisplayName("Logout")
@Feature("Logout")
@Story("API")
public class LogoutTest extends BaseTest {



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

    @RepeatedTest(5)
    @DisplayName("Проверка logout не успех")
    @Description("Проверяем неуспешный ответ на logout")
    @Owner("Marin")
    @Severity(SeverityLevel.CRITICAL)
    void testActionFail(){
        String token = testUtils.genToken();

        Allure.step("Шаг 3. Делаем запрос logout для успешного ответа", () -> {
            Response response = RestAssured.given()
                    .spec(specForEndpoint)
                    .header(ParamName.ACTION, ActionName.ACTION)
                    .header(ParamName.TOKEN, token)
                    .post(EndpointsService.ENDPOINT);

            allureUtils.forAllure(response, token);

            response.then()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("result", equalTo("ERROR"))
                    .body("message", equalTo(String.format("Token '%s' not found", token)));
        });
    }
}
