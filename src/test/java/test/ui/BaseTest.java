package test.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BaseTest {


    public Playwright getPlaywright() {
        playwright = Playwright.create();
        return playwright;
    }

    public Browser getBrowser() {
        if (browser==null){
            browser = getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            return browser;
        }
        return browser;
    }


    private Playwright playwright;
    private Browser browser;



}


