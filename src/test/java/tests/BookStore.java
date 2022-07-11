package tests;

import data.UserData;
import io.qameta.allure.Description;
import models.Credentials;
import models.GetBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

@Tag("bookStore")
public class BookStore extends TestBase {
    static Credentials credentials = new Credentials();
    static UserData userdata = new UserData();

    @Test
    @Tag("bookStore")
    @DisplayName("Проверка списка книг. Код ответа 200")
    @Description("Запрос - /BookStore/v1/Books")
    void getBooks() {
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }

    @Test
    @Tag("bookStore")
    @DisplayName("Проверка существования пользователя")
    @Description("Запрос - /Account/v1/User")
    void checkUserExists() {
        credentials.setUserName(userdata.getUsername());
        credentials.setPassword(userdata.getPassword());
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/User")
                .then()
                .log().status()
                .log().body()
                .statusCode(406)
                .body("message", is("User exists!"))
                .body("code", is("1204"));
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка неавторизованного пользователя")
    @Description("Запрос - /Account/v1/User/UUID")
    void getUserUUID() {
        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/Account/v1/User/UUID={idBook}", userdata.getUserId())
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(401)
                        .extract().as(GetBook.class);
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка авторизации с корректными данными")
    @Description("Запрос - /Account/v1/Login")
    void authSuccess() {
        credentials.setUserName(userdata.getUsername());
        credentials.setPassword(userdata.getPassword());
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .log().all();
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка авторизации с не существующим UserName - Authorized")
    @Description("Запрос /Account/v1/Authorized")
    void authUserInvalid2() {
        credentials.setUserName(userdata.getUsernameFalse());
        credentials.setPassword(userdata.getPassword());

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/Authorized")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body("message", is("User not found!"))
                .body("code", is("1207"));
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка авторизации с некорректным паролем")
    @Description("Запрос - /Account/v1/Authorized")
    void authInvalidPassword() {
        credentials.setUserName(userdata.getUsername());
        credentials.setPassword(userdata.getInvalidPassword());

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/Authorized")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body("message", is("User not found!"))
                .body("code", is("1207"));
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка запроса с параметром и проверка Title"
    )
    @Description("Запрос /BookStore/v1/Book")
    void getBookParameter() {

        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/BookStore/v1/Book?ISBN={idBook}", userdata.getIdBook())
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(GetBook.class);

        assertThat(getBookResponse.getIsbn()).isEqualTo(userdata.getIdBook());
        assertThat(getBookResponse.getTitle()).isEqualTo(userdata.getBookTitle());
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Проверка запроса с параметром  и проверка всех параметров в ответе"
    )
    @Description("Запрос /BookStore/v1/Book")
    void getBookAllParameter() {
        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/BookStore/v1/Book?ISBN={idBook}", userdata.getIdBook())
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(GetBook.class);

        assertThat(getBookResponse.getIsbn()).isEqualTo(userdata.getIdBook());
        assertThat(getBookResponse.getTitle()).isEqualTo(userdata.getBookTitle());
        assertThat(getBookResponse.getSubTitle()).isEqualTo(userdata.getBookSubTitle());
        assertThat(getBookResponse.getAuthor()).isEqualTo(userdata.getBookAuthor());
        assertThat(getBookResponse.getPublish_date()).isEqualTo(userdata.getBookPublish_date());
        assertThat(getBookResponse.getPublisher()).isEqualTo(userdata.getBookPublisher());
        assertThat(getBookResponse.getPages()).isEqualTo(userdata.getBookPages());
        assertThat(getBookResponse.getDescription()).isEqualTo(userdata.getBookDescription());
        assertThat(getBookResponse.getWebsite()).isEqualTo(userdata.getBookWebsite());
    }
}
