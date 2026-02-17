package com.example.testutils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class AllureUtils {

    public void forAllure(Response response){

        Allure.addAttachment(
                "Status code",
                String.valueOf(response.getStatusCode())
        );

        Allure.addAttachment(
                "Response body",
                response.getBody().asPrettyString()
        );
    }

    public void forAllure(Response response, String token){
        Allure.addAttachment("Token", token);
        forAllure(response);
    }

}
