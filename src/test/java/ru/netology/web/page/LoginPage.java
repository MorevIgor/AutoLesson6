package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {
    private SelenideElement loginField = $x("//*[@data-test-id=\"login\"]//self::input");
    private SelenideElement passwordField = $x("//*[@data-test-id=\"password\"]//self::input");
    private SelenideElement loginButton = $x("//*[@data-test-id=\"action-login\"]");


    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
}
