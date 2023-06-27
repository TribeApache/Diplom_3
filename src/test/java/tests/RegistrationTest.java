package tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.Augmenter;

import page.MainPage;
import page.UserClient;

import static org.junit.Assert.assertTrue;

public class RegistrationTest {
    private WebDriver driver;
    private UserClient userClient;
    private MainPage page;
    private String name;
    private String email;
    private String password;
    private String passwordIncorrect;
    private int count = 10;
    private int countForPasswordIncorrect = 1;

    @Before
    public void startUp() {
        String browser = System.getProperty("browser", "chrome");
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("yandex")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setBinary("C:\\Users\\user\\Desktop\\Diplom_Artemyev_Constantine_19\\Diplom_3\\src\\main\\resources\\drivers\\yandexdriver.exe");
            driver = new ChromeDriver(options);
        }

        userClient = new UserClient();
        page = new MainPage(driver);
        name = RandomStringUtils.randomAlphabetic(count);
        email = RandomStringUtils.randomAlphabetic(count) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(count);
        passwordIncorrect = RandomStringUtils.randomAlphabetic(countForPasswordIncorrect);
    }

    @After
    public void teardown() {
        // получение токена для удаления на главной странице после логина
        page.open()
                .clickPersonalAccountButton()
                .logInUser(email, password);
        // Получение токена для удаления созданного пользователя
        WebStorage webStorage = (WebStorage) new Augmenter().augment(driver);
        LocalStorage localStorage = webStorage.getLocalStorage();
        String accessToken = localStorage.getItem("accessToken");
        userClient.deleteUser(accessToken);
        // Закрытие браузера
        driver.quit();
    }

    @Test
    public void registrationWithCorrectPasswordTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .clickRegistrationButton()
                .registrationUser(name, email, password)
                .expectPersonalAccount()
                .checkElementIn();
        assertTrue(flag);
    }

    @Test
    public void registrationWithExistUserTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .clickRegistrationButton()
                .registrationUser(name, email, password)
                .expectPersonalAccount()
                .clickRegistrationButton()
                .registrationUserError(name, email, password)
                .scroll()
                .checkErrorRegistrationExistUser();
        assertTrue(flag);
    }

    @Test
    public void registrationWithIncorrectPasswordTest() {
        boolean flag = page.open()
                .clickPersonalAccountButton()
                .clickRegistrationButton()
                .registrationUserError(name, email, passwordIncorrect)
                .checkErrorRegistrationIncorrectPassword();
        assertTrue(flag);
    }
}
