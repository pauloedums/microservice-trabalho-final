package br.com.impacta.microservices.ib;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ClientsResourceTest {

    @Test
    public void clientWithNoAuthorization() {
        given()
        .when().get("/api/clients/me")
        .then()
            .statusCode(401)
            .body(is(""));
    }

}