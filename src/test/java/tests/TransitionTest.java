package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import page.MainPage;

import static org.junit.Assert.assertTrue;

public class TransitionTest {

    private WebDriver driver;
    private MainPage page;
    private int count = 10;

    @Before
    public void startUp() {
        String browser = System.getProperty("browser", "chrome");
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
        }
        else if (browser.equalsIgnoreCase("yandex")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setBinary("C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\yandexdriver.exe");
            driver = new ChromeDriver(options);
        }

        page = new MainPage(driver);
    }

    @Test
    public void logInTransitionInPersonalAccountTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .expectPersonalAccount()
                .checkElementIn();
        assertTrue(flag);
    }

    @Test
    public void logInTransitionInMainPageAcrossConstructorTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .expectPersonalAccount()
                .clickConstructorButton()
                .expectMainPageLogInAccount()
                .checkButtonLogInAccount();
        assertTrue(flag);
    }

    @Test
    public void logInTransitionInMainPageAcrossLogoTipTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .expectPersonalAccount()
                .clickButtonLogo()
                .expectMainPageLogInAccount()
                .checkButtonLogInAccount();
        assertTrue(flag);
    }

    @Test
    public void transitionInSousTest() {
        boolean flag = page.open()
                .clickSousButton()
                .checkButtonSousLight();
        assertTrue(flag);
    }

    @Test
    public void transitionInBulkTest() {
        boolean flag = page.open()
                .clickSousButton()
                .clickBulksButton()
                .checkButtonBulksLight();
        assertTrue(flag);
    }

    @Test
    public void transitionInFillingsTest() {
        boolean flag = page.open()
                .clickFillingsButton()
                .checkButtonFillingsLight();
        assertTrue(flag);
    }

    @After
    public void teardown() {
        // Закрытие браузера
        driver.quit();
    }
}