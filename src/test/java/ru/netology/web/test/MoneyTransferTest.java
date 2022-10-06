package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    String cardFrom1 = String.valueOf(DataHelper.getCardInfo("1"));
    String cardFrom2 = String.valueOf(DataHelper.getCardInfo("2"));
    String cardFrom0 = String.valueOf(DataHelper.getCardInfo(""));

    public void balancedBalance() {
        var dashboardPage = new DashboardPage();
        int money1 = dashboardPage.getCardBalance("1");
        int money2 = dashboardPage.getCardBalance("2");
        int aMoney = (money1 + money2) / 2;
        if (money1 < aMoney) {
            int aAMoney = aMoney - money1;
            dashboardPage.transferSecondToFirst();
            var transferMoney = new TransferPage();
            transferMoney.transfer(aAMoney, cardFrom2);
        } else if (money2 < aMoney) {
            int aAMoney = aMoney - money2;
            dashboardPage.transferFirstToSecond();
            var transferMoney = new TransferPage();
            transferMoney.transfer(aAMoney, cardFrom1);
        }
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        balancedBalance();
    }

    @Test
    void transferSecondToFirstCard() {  //перевод со второго счёта на первый
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("2"));
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") + money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("2") - money;
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom2);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");

        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferFirstToSecondCard() {  //перевод с первого счёта на второй
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("1"));
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") - money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("1") + money;
        dashboardPage.transferFirstToSecond();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom1);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");

        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void errorNotAccountNumber() {  //перевод без указания номера счёта
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("1"));
        dashboardPage.transferFirstToSecond();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom0);
        transferMoney.getErrorNotAccountNumber();
    }

    @Test
    void transferMax() {    //перевод всей  суммы
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("2")) * 2;
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") + money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("2") - money;
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom2);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferOverMax() {    //перевод большей суммы чем есть на счете
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("2")) * 3;
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom2);
        transferMoney.getErrorOther();     // должен быть текст ошибки
    }

    @Test
    void transferNotMoney() {  // перевод без указания суммы
        var dashboardPage = new DashboardPage();
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(0, cardFrom2);
        transferMoney.getErrorOther();      //должен быть текст ошибки
    }

    @Test
    void transferSecondToSecondAccountNumbers() {  //перевод на тот же счёт
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("2"));
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom1);
        transferMoney.getErrorOther();      //должен быть текст ошибки
    }

    @Test
    void nullBalance() {        //перевод при нулевом балансе
        var dashboardPage = new DashboardPage();
        int money = DataHelper.howManyMoney(dashboardPage.getCardBalance("1")) * 2;
        dashboardPage.transferSecondToFirst();
        var transferMoney = new TransferPage();
        transferMoney.transfer(money, cardFrom2);
        dashboardPage.transferSecondToFirst();
        var transferMoney1 = new TransferPage();            //страница перевода появляется 2 раза
        transferMoney1.transfer(money, cardFrom2);
        transferMoney.getErrorOther();      //должен быть текст ошибки
    }

    @Test
    void reloadData() {     //кнопка обновить
        var dashboardPage = new DashboardPage();
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1");
        int expectedBalanceCard2 = dashboardPage.getCardBalance("2");
        dashboardPage.reloadData();
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }
}

