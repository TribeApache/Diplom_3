package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;

@RunWith(Parameterized.class)
public class RegistrationTest {

    private WebDriver driver;
    private String driverType;

    public RegistrationTest(String driverType){
        this.driverType = driverType;
        System.setProperty(
                "webdriver.chrome.driver",
                "src\\main\\resources\\drivers\\" + this.driverType + ".exe"
        );
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site");
    }

    @Parameterized.Parameters(name="driver: {0}")
    public static Object[][] getDriver(){
        return new Object[][]{
                {"chromedriver"},
                {"yandexdriver"},
        };
    }

    @Test
    @DisplayName("Successful user registration")
    @Description("Check if the user is successfully registered and if the 'Login' text is visible on the login page")
    public void successRegistrationTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        String name = RandomStringUtils.randomAlphanumeric(4, 8);
        String email = RandomStringUtils.randomAlphanumeric(6, 10) + "@" + RandomStringUtils.randomAlphanumeric(2, 8) + ".ru";
        String password = RandomStringUtils.randomAlphanumeric(10, 20);
        registerPage.registration(name, email, password);
        loginPage.waitForLoadEntrance();
        Assert.assertTrue("The registration fields are filled out incorrectly. The login page is not displayed",
                driver.findElement(loginPage.entrance).isDisplayed());
    }


    @Test
    @DisplayName("Unsuccessful user registration.")
    @Description("Check for unsuccessful user registration and catch the text error 'Incorrect password' on the registration page when entering a password of less than 6 characters")
    public void failedRegistrationTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoadRegisterPage();
        String name = RandomStringUtils.randomAlphanumeric(4, 8);
        String email = RandomStringUtils.randomAlphanumeric(6, 10) + "@" + RandomStringUtils.randomAlphanumeric(2, 8) + ".ru";
        String password = RandomStringUtils.randomAlphanumeric(0, 5);
        registerPage.registration(name, email, password);
        Assert.assertTrue("The password was entered correctly. Registration was successful",
                driver.findElement(registerPage.errorPasswordText).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }


}
