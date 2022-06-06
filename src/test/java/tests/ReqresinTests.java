package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresinTests {

    String baseUrl = "https://reqres.in",
            loginUrl = "/api/login",
            usersUrl = "/api/users",
            users2Url = "/api/users/2",
            createUserBody = "{\"name\": \"morpheus\",     \"job\": \"leader\" }",
            updateUserBody = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}",
            loginBody = "{ \"email\": \"eve.holt@reqres.in\", " +
                    "\"password\": \"cityslicka\" }",
            missingPasswordBody = "{ \"email\": \"eve.holt@reqres.in\"}",
            token = "QpwL5tke4Pnpja7X4",
            error = "Missing password";

    @Test
    void getUsers() {
        given()
                .when()
                .get(baseUrl + usersUrl)
                .then()
                .log().all()
                .body("page", is(1))
                .body("per_page", is(6))
                .body("total", is(12))
                .body("total_pages", is(2));
    }

    @Test
    void createUser() {
        given()
                .body(createUserBody)
                .contentType(JSON)
                .when()
                .post(baseUrl + usersUrl)
                .then()
                .log().all()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        given()
                .body(updateUserBody)
                .contentType(JSON)
                .when()
                .put(baseUrl + users2Url)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void loginTest() {
        given()
                .log().uri()
                .log().body()
                .body(loginBody)
                .contentType(JSON)
                .when()
                .post(baseUrl + loginUrl)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is(token));
    }

    @Test
    void missingPasswordLoginTest() {
        given()
                .log().uri()
                .log().body()
                .body(missingPasswordBody)
                .contentType(JSON)
                .when()
                .post(baseUrl + loginUrl)
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is(error));
    }
}
