package test.ui;

import po.LoginPage;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import org.testng.annotations.Test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GetLoginTokenTest extends BaseTest {
    private List<Cookie> cookies;
    private static BrowserContext bc;
    private Properties properties = new Properties();
    FileInputStream input;

    {
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    String userLogin = properties.getProperty("userLogin");
    String userPass= properties.getProperty("userPass");

    String url = "https://if.db-test.vtb.ru/login";
    String customsURL = "https://if.db-test.vtb.ru/ved/customs-services/";

    LoginPage lp = new LoginPage();

    @Test
    public void loginTest() {
        Browser browser = getBrowser();
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        page.navigate(url);

        Locator login = page.locator(lp.getLogin());
        Locator pass = page.locator(lp.getPass());
        Locator submitBtn = page.locator(lp.getSubmitBtn());
        Locator mainMenu = page.locator(lp.getMainMenu());

        login.fill(userLogin);
        pass.fill(userPass);
        submitBtn.click();

        mainMenu.waitFor(new Locator.WaitForOptions().setTimeout(20000));
        assertThat(mainMenu).isVisible();

        page.navigate(customsURL);
        cookies = browserContext.cookies();
    }

    @Test
    public void secondTest() throws InterruptedException {
        Browser browser = getBrowser();

        BrowserContext context = browser.newContext();
        context.addCookies(cookies);

        Page page = context.newPage();
        page.navigate(customsURL);
//        Thread.sleep(10000);

    }
}


