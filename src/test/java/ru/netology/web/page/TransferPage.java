package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class TransferPage {
    private SelenideElement valueMoney = $x("//span[@data-test-id=\"amount\"]//self::input");
    private SelenideElement valueAccountNumber = $x("//*[@data-test-id=\"from\"]//self::input");
    private SelenideElement clickButtonAccept = $x("//*[@data-test-id=\"action-transfer\"]");
    private SelenideElement errorMessage = $x("//*[@data-test-id=\"error-notification\"]");

    public DashboardPage transfer(int money, String cardFrom) {
        valueMoney.sendKeys(Keys.LEFT_CONTROL + "A" + Keys.DELETE);
        valueMoney.setValue(String.valueOf(money));
        valueAccountNumber.sendKeys(Keys.LEFT_CONTROL + "A" + Keys.DELETE);
        valueAccountNumber.setValue(cardFrom);
        clickButtonAccept.click();
        return new DashboardPage();
    }

    public void getErrorNotAccountNumber() {
        errorMessage.shouldHave(text("Ошибка! Произошла ошибка"));
    }

    public void getErrorOther() {
        errorMessage.shouldHave(text("Заглушка"));
    }

}
