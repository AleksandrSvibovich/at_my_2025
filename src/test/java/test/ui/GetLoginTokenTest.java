package test.ui;

import po.LoginPage;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import org.testng.annotations.Test;


import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GetLoginTokenTest extends BaseTest {
    private List<Cookie> cookies;
    private static BrowserContext bc;

    String userLogin = "dbo.svibov_at17";
    String userPass = "Qazwsx123!";

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


