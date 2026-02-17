package com.example.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.http.ContentType;
import lombok.Data;
import org.junit.jupiter.api.AfterAll;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Data
public class WireMockConfig {

    public static WireMockServer wireMockServer;

    public static void startWireMockOk() {
        startWm("localhost", 8888);
        stubAuth(200,"{\"status\":\"ok\"}");
        stubDoAction(200,"{\"status\":\"ok\"}");

    }

    @AfterAll
    public static void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }


    public static void stubAuth(int statusCode, String body) {
        stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", ContentType.JSON.getAcceptHeader())
                        .withBody(body)));
    }

    public static void stubDoAction(int statusCode, String body) {
        stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", ContentType.JSON.getAcceptHeader())
                        .withBody(body)));
    }

    private static void startWm(String host,int port) {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
        configureFor(host, port);
    }
}
