package ru.netology.web.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private ElementsCollection depositButton = $$x("//*[@data-test-id=\"action-deposit\"]");

    private SelenideElement massageErrorAccountNumber = $x("//*[@class=\"notification__content\"]");
    private SelenideElement reloadData = $x("//*[@data-test-id=\"action-reload\"]//child::span[@class=\"button__text\"]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement firstDeposite = $x("//*[@data-test-id=\"92df3f1c-a033-48e6-8390-206f6b1f56c0\"]");

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }


    public int getCardBalance(String id) {
        String cardBalance = cards.findBy(text(DataHelper.getCardInfo(id).getNumber().substring(16))).getText();
        return extractBalance(cardBalance);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void transferFirstToSecond() {
        depositButton.get(1).click();
    }

    public void transferSecondToFirst() {
        depositButton.get(0).click();
    }

    public void reloadData() {
        reloadData.click();
    }

}
