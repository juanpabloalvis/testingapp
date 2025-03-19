package com.jp.testingapp.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jp.testingapp.AbstractionBaseTest;
import com.jp.testingapp.service.ExternalServiceClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExternalServiceClientTest extends AbstractionBaseTest {

    private WireMockServer wireMockServer;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @BeforeEach
    public void setup() {
        // Iniciar el servidor WireMock en un puerto específico
        wireMockServer = new WireMockServer(8089); // Puedes usar 0 para un puerto aleatorio
        wireMockServer.start();

        // Configurar el comportamiento del mock
        wireMockServer.stubFor(get(urlEqualTo("/api/external"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Hello from WireMock with RestClient\"}")));
    }

    @AfterEach
    public void teardown() {
        // Detener el servidor WireMock después de cada prueba
        wireMockServer.stop();
    }

    @Test
    public void testCallExternalService() {
        // Crear una instancia del cliente con el RestClient
        RestClient restClient = restClientBuilder.baseUrl("http://localhost:8089").build();
        ExternalServiceClient client = new ExternalServiceClient(restClient);

        // Llamar al servicio simulado
        String response = client.callExternalService("/api/external");

        // Verificar la respuesta
        assertEquals("{\"message\": \"Hello from WireMock with RestClient\"}", response);

        // Verificar que el mock fue llamado
        wireMockServer.verify(getRequestedFor(urlEqualTo("/api/external")));
    }
}