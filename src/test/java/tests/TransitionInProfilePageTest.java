package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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
import pages.ProfilePage;

@RunWith(Parameterized.class)
public class TransitionInProfilePageTest {

    private WebDriver driver;
    private String driverType;
    private final static String EMAIL = "test-data@yandex.ru";
    private final static String PASSWORD = "password";

    public TransitionInProfilePageTest(String driverType){
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
    @DisplayName("Go to login page")
    @Description("Checking the login page transition and visibility of the 'Вход' header on it")
    public void transitionToProfilePageTest(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        Assert.assertTrue("The authorization page has not been displayed",
                driver.findElement(loginPage.entrance).isDisplayed());
    }

    @Test
    @DisplayName("Transition to the personal account page with authorization")
    @Description("Checking the transition to the personal cabinet page of the registered and authorized user and visibility of the 'Вход' header on it")
    public void transitionToProfilePageWithAuthUserTest(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.authorization(EMAIL, PASSWORD);
        mainPage.waitForLoadMainPage();
        mainPage.clickOnAccountButton();
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoadProfilePage();
        Assert.assertTrue("The personal account page did not load",
                driver.findElement(profilePage.textOnProfilePage).isDisplayed());
    }

    @Test
    @DisplayName("Click on the 'Stellar Burgers' logo")
    @Description("Check the transition to the home page by clicking on the logo from the login page, and the visibility of the text on the 'Assemble Burger' home page.")
    public void transitionToStellarBurgersFromProfilePageTest(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnLogo();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("The home page didn't load.", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("'Выход' from my personal account")
    @Description("Check the 'Выход' button in the personal cabinet and the visibility of the 'Вход' header text on the authorization page.")
    public void exitFromProfileTest(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.authorization(EMAIL,PASSWORD);
        mainPage.waitForLoadMainPage();
        mainPage.clickOnAccountButton();
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoadProfilePage();
        profilePage.clickOnExitButton();
        mainPage.waitForInvisibilityLoadingAnimation();
        Assert.assertTrue("Failed to sign out of the account", driver.findElement(loginPage.entrance).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
