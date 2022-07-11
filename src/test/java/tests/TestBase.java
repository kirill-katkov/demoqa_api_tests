package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;
import owner.LinksConfigOwner;
import owner.RemoteConfigOwner;

import static com.codeborne.selenide.Selenide.closeWebDriver;
public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://demoqa.com/";
        Configuration.baseUrl = "https://demoqa.com/";
    }
}
