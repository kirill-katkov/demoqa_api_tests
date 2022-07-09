package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import models.Credentials;
import models.GetBook;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import owner.UserConfigOwner;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.*;
@Tag("bookStore")
public class BookStore extends TestBase {
    static Credentials credentials = new Credentials();

//godinew9
    //565656qqA!
    //{"userId":"293ed243-0866-49a8-a620-96af443e66f7","username":"godinew9",
//    @BeforeAll
//    static void beforeAll() {
//        RestAssured.baseURI = "https://demoqa.com";
//        System.setProperty("com.sun.security.enableAIAcaIssuers", "true");
//        String certificatesTrustStorePath = "<JAVA HOME>/jre/lib/security/cacerts";
//        System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
//    }


    @Test
    @Tag("bookStore")
    @DisplayName("Запрос /BookStore/v1/Books")
    @Description("Проверка запроса - список книг.  Код ответа")
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
    @DisplayName("Запрос /Account/v1/User")
    @Description("Проверка существующего пользователя")
    void checkUserExists() {
        credentials.setUserName("godinew9");
        credentials.setPassword("565656qqA!");

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
    @DisplayName("Запрос /Account/v1/User/UUID")
    @Description("Проверка неавторизованного пользователя ")
    void getBook33() {
        String idBook = "293ed243-0866-49a8-a620-96af443e66f7";

        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/Account/v1/User/UUID={idBook}", idBook)
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(401)
                        .extract().as(GetBook.class);
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Запрос /Account/v1/Login")
    @Description("Проверка авторизации с корректными данными")
    void authSuccess() {
        credentials.setUserName("godinew9");
        credentials.setPassword("565656qqA!");

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
    @DisplayName("Запрос /Account/v1/GenerateToken")
    @Description("Проверка авторизации с не существующим UserName")
    void authUserInvalid() {
        credentials.setUserName("godinew945");
        credentials.setPassword("565656qqA!");

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("result", is("User authorization failed."))
           .body("status", is("Failed"));
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Запрос /Account/v1/Authorized")
    @Description("Проверка авторизации с не существующим UserName")
    void authUserInvalid2() {
        credentials.setUserName("godinew945");
        credentials.setPassword("565656qqA!");

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
    @DisplayName("Запрос /Account/v1/Authorized")
    @Description("Проверка авторизации с некорректным паролем")
    void authInvalidPassword() {;
        credentials.setUserName("godinew9");
        credentials.setPassword("565656wewq");

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
    @DisplayName("Запрос /BookStore/v1/Book")
    @Description("Проверка запроса с заданным параметром ISBN=9781449325862. " +
            "Код ответа. Проверка параметра Title"
    )
    void getBookParameter() {
        String idBook = "9781449325862";

        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/BookStore/v1/Book?ISBN={idBook}", idBook)
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(GetBook.class);

        assertThat(getBookResponse.getIsbn()).isEqualTo(idBook);
        assertThat(getBookResponse.getTitle()).isEqualTo("Git Pocket Guide");
    }


    @Test
    @Tag("bookStore")
    @DisplayName("Запрос /BookStore/v1/Book")
    @Description("Проверка запроса с заданным параметром ISBN=9781449325862. " +
            "Код ответа. Проверка всех параметров в ответе"
    )
    void getBookAllParameter() {
        String idBook = "9781449325862";

        GetBook getBookResponse =
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().body()
                        .get("/BookStore/v1/Book?ISBN={idBook}", idBook)
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(GetBook.class);

        assertThat(getBookResponse.getIsbn()).isEqualTo(idBook);
        assertThat(getBookResponse.getTitle()).isEqualTo("Git Pocket Guide");
        assertThat(getBookResponse.getSubTitle()).isEqualTo("A Working Introduction");

        assertThat(getBookResponse.getAuthor()).isEqualTo("Richard E. Silverman");
        assertThat(getBookResponse.getPublish_date()).isEqualTo("2020-06-04T08:48:39.000Z");
        assertThat(getBookResponse.getPublisher()).isEqualTo("O'Reilly Media");
        assertThat(getBookResponse.getPages()).isEqualTo("234");
        assertThat(getBookResponse.getDescription()).isEqualTo("This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp");
        assertThat(getBookResponse.getPages()).isEqualTo("234");
        assertThat(getBookResponse.getWebsite()).isEqualTo("http://chimera.labs.oreilly.com/books/1230000000561/index.html");
    }


}
