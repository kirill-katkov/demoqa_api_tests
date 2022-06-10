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
        LinksConfigOwner linkConfig= ConfigFactory.create(LinksConfigOwner.class);

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = linkConfig.uiURL();
        RestAssured.baseURI = linkConfig.apiURI();
        RemoteConfigOwner confRemote = ConfigFactory.create(RemoteConfigOwner.class);

        String propertyBrowserSize = System.getProperty("browserSize", "1980x1024"),
                propertyRemoteUrl = System.getProperty("remoteUrl", confRemote.url());


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;

        Configuration.browserSize = propertyBrowserSize;
        Configuration.remote = propertyRemoteUrl;

    }

    @AfterEach
    void afterEach() {
        Attach.screenshotAs("Скриншот выполненного теста");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }

}
