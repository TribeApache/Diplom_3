package tests;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import page.MainPage;
import page.User;
import page.UserClient;

import static org.junit.Assert.assertTrue;

public class LogInTest {
    private WebDriver driver;
    private UserClient userClient;
    private MainPage page;
    private User user;
    private String name;
    private String email;
    private String password;
    private String accessToken;
    private int count = 10;
    @Before
    public void startUp() {
        name = RandomStringUtils.randomAlphabetic(count);
        email = RandomStringUtils.randomAlphabetic(count) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(count);
        user = new User(email, password, name);
        userClient = new UserClient();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");

        String browserName = System.getProperty("browser", "chrome");

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("yandex")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setBinary("C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\yandexdriver.exe");
            driver = new ChromeDriver(options);
        }

        page = new MainPage(driver);
    }

    @Test
    public void logInAcrossButtonOnMainTest() {
        boolean flag = page.open()
                .clickGoInAccountButton()
                .logInUser(email, password)
                .expectMainPage()
                .checkButtonCreateOrder();
        assertTrue(flag);
    }

    @Test
    public void logInAcrossPersonalAccountTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .logInUser(email, password)
                .expectMainPage()
                .checkButtonCreateOrder();
        assertTrue(flag);
    }

    @Test
    public void logInAcrossFormRegistrationTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .clickRegistrationButton()
                .clickButtonInOnPageRegistration()
                .logInUser(email, password)
                .expectMainPage()
                .checkButtonCreateOrder();
        assertTrue(flag);
    }

    @Test
    public void logInAcrossFormReanimatePasswordTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .clickReanimatePasswordButton()
                .clickLogInButton()
                .logInUser(email, password)
                .expectMainPage()
                .checkButtonCreateOrder();
        assertTrue(flag);
    }

    @Test
    public void logOutTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .logInUser(email, password)
                .expectMainPage()
                .clickPersonalAccountButton()
                .expectPersonalAccountButtonLogout()
                .clickLogOutButton()
                .expectPersonalAccount()
                .checkElementIn();
        assertTrue(flag);
    }

    @After
    public void teardown() {
        //Удаление user
        userClient.deleteUser(accessToken);
        // Закрытие браузера
        driver.quit();
    }
}