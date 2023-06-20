package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.MainPage;


@RunWith(Parameterized.class)
public class TransitionInConstructorTest {

    private final WebDriver driver;
    private final String driverType;

    public TransitionInConstructorTest(String driverType) {
        this.driverType = driverType;
        System.setProperty(
                "webdriver.chrome.driver",
                "src\\main\\resources\\drivers\\" + this.driverType + ".exe"
        );
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site");
        new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[contains(@class,'_menuContainer')]//ul)[last()]/a")));
    }

    @Parameterized.Parameters(name = "driver: {0}")
    public static Object[][] getDriver() {
        return new Object[][]{
                {"chromedriver"},
                {"yandexdriver"},
        };
    }

    @Test
    @DisplayName("Go to the designer from your personal cabinet")
    @Description("Checking the switch to the 'Конструктор' tab from the user authorization page.")
    public void transitionToConstructorFromProfilePageTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnConstructorButton();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("Switching to the designer page did not go through", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("Going to the 'Булки' tab")
    @Description("Checking the switch to the 'Булки' tab and the visibility of the first 'булкой' picture.")
    public void transitionToBunsInConstructorTest() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnFillingButton();
        mainPage.waitForLoadFillingsHeader();
        //без ожидания тест падает
        Thread.sleep(100);
        mainPage.clickOnBunsButton();
        mainPage.waitForLoadBunsHeader();
        Assert.assertTrue("The block with the 'булками' is not visible.", driver.findElement(mainPage.bunsImg).isDisplayed());
    }

    @Test
    @DisplayName("Go to the 'Соусы' tab")
    @Description("Checking the switch to the 'Соусы' tab and the visibility of the 'соусом' picture.")
    public void transitionToSaucesInConstructorTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnSaucesButton();
        mainPage.waitForLoadSaucesHeader();
        Assert.assertTrue("The 'соусами' block is not visible.", driver.findElement(mainPage.saucesImg).isDisplayed());
    }

    @Test
    @DisplayName("Go to the 'Начинки' tab")
    @Description("Checking the transition to the 'Начинки' tab and the visibility of the 'начинкой' picture.")
    public void transitionToFillingsInConstructorTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnFillingButton();
        mainPage.waitForLoadFillingsHeader();
        Assert.assertTrue("The 'соусами' block is not visible.", driver.findElement(mainPage.fillingsImg).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
