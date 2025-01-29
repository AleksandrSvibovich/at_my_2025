package po;

public class LoginPage extends BasePage {

//    Page page = browserContext.newPage();
//    page.navigate("https://if.db-test.vtb.ru/login");


    public String getLogin() {
        return "xpath=//input[@for='userName']";
    }

    public String getPass() {
        return "//input[@for='password']";
    }

    public String getSubmitBtn() {
        return "//button[@data-testid='login-form-submit-button']";
    }
}
