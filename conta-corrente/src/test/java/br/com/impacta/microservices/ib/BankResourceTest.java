package br.com.impacta.microservices.ib;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BankResourceTest {

    @Test
    public void testBankResource() {
        given()
        .when().get("/api/admin")
        .then()
            .statusCode(401)
            .body(is(""));
    }

}