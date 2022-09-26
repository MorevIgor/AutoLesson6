package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    public static Object transfer;
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private static SelenideElement firstCard = $x("//*[@class=\"list__item\"]//child::div[@data-test-id=\"92df3f1c-a033-48e6-8390-206f6b1f56c0\"]");
    private static SelenideElement secondCard = $x("//*[@class=\"list__item\"]//child::div[@data-test-id=\"0f3f5c2a-249e-4c3d-8287-09f7a039391d\"]");

    private static SelenideElement clickButtonDepositeCard1 = $x("//*[@data-test-id=\"92df3f1c-a033-48e6-8390-206f6b1f56c0\"]//child::button[@data-test-id=\"action-deposit\"]");
    private static SelenideElement clickButtonDepositeCard2 = $x("//*[@data-test-id=\"0f3f5c2a-249e-4c3d-8287-09f7a039391d\"]//child::button[@data-test-id=\"action-deposit\"]");
    private static SelenideElement valueMoney = $x("//span[@data-test-id=\"amount\"]//self::input");
    private static SelenideElement valueAccountNumber = $x("//*[@data-test-id=\"from\"]//self::input");
    private static SelenideElement clickButtonAccept = $x("//*[@data-test-id=\"action-transfer\"]");
    private static SelenideElement massageErrorAccountNumber = $x("//*[@class=\"notification__content\"]");
    private static SelenideElement reloadData = $x("//*[@data-test-id=\"action-reload\"]//child::span[@class=\"button__text\"]");
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";

    public static int getFirstCardBalance() {
        val text = firstCard.text();
        return extractBalance(text);
    }

    public static int getSeconCardBalance() {
        val text = secondCard.text();
        return extractBalance(text);
    }

    private static int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public static DashboardPage transfer(int money, int id,int idN) {
        if (id == 2) {
            clickButtonDepositeCard2.click();
        } else if (id == 1){
            clickButtonDepositeCard1.click();
        }
        valueMoney.sendKeys(Keys.LEFT_CONTROL + "A" + Keys.DELETE);
        valueMoney.setValue(String.valueOf(money));
        valueAccountNumber.sendKeys(Keys.LEFT_CONTROL + "A" + Keys.DELETE);
        if (idN == 1) {
            valueAccountNumber.setValue(DataHelper.getNumberCard1().getNumberCard());
        } else if (idN == 2) {
            valueAccountNumber.setValue(DataHelper.getNumberCard2().getNumberCard());
        } else {
            valueAccountNumber.setValue("");
        }
        clickButtonAccept.click();
        return new DashboardPage();
    }

    public static int howManyMoney(int id) {
        int money = 0;
        if (id == 1) {
            money = DashboardPage.getSeconCardBalance() / 2;
        } else {
            money = DashboardPage.getFirstCardBalance() / 2;
        }
        return money;
    }

    public static DashboardPage reloadData() {
        reloadData.click();
        return null;
    }

    public static DashboardPage balancedBalance(){
        int money1 = DashboardPage.getFirstCardBalance();
        int money2 = DashboardPage.getSeconCardBalance();
        int aMoney = (money1 + money2) / 2;
        if (money1<aMoney){
            int aAMoney = aMoney - money1;
            transfer(aAMoney,1,2);
        } else if(money2<aMoney){
            int aAMoney = aMoney -  money2;
            transfer(aAMoney,2,1);
        }
        return null;
    }
}
