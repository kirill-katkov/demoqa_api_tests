package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://demoqa.com/";
        Configuration.baseUrl = "https://demoqa.com/";
    }
}
