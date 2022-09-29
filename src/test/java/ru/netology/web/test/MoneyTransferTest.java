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
    public void balancedBalance(){
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money1 = dashboardPage.getCardBalance("1");
        int money2 = dashboardPage.getCardBalance("2");
        int aMoney = (money1 + money2) / 2;
        if (money1<aMoney){
            int aAMoney = aMoney - money1;
            dashboardPage.transferSecondToFirst();
            transferMoney.transfer(aAMoney,2);
        } else if(money2<aMoney){
            int aAMoney = aMoney -  money2;
            dashboardPage.transferFirstToSecond();
            transferMoney.transfer(aAMoney,1);
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
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("2");
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") + money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("2") - money;
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money,2);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");

        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferFirstToSecondCard() {  //перевод с первого счёта на второй
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("1");
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") - money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("1") + money;
        dashboardPage.transferFirstToSecond();
        transferMoney.transfer(money,1);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");

        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void errorNotAccountNumber() {  //перевод без указания номера счёта
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("1");
        dashboardPage.transferFirstToSecond();
        transferMoney.transfer(money,0);
        transferMoney.getErrorNotAccountNumber();
    }

    @Test
    void transferMax() {    //перевод всей  суммы
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("2") * 2;
        int expectedBalanceCard1 = dashboardPage.getCardBalance("1") + money;
        int expectedBalanceCard2 = dashboardPage.getCardBalance("2") - money;
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money, 2);
        int actualBalanceCard1 = dashboardPage.getCardBalance("1");
        int actualBalanceCard2 = dashboardPage.getCardBalance("2");
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferOverMax() {    //перевод большей суммы чем есть на счете
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("2") * 3;
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money, 2);
        transferMoney.getErrorOther();     // должен быть текст ошибки
    }

    @Test
    void transferNotMoney() {  // перевод без указания суммы
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(0,2);
        transferMoney.getErrorOther();      //должен быть текст ошибки
    }

    @Test
    void transferSecondToSecondAccountNumbers() {  //перевод на тот же счёт
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("2");
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money, 1);
        transferMoney.getErrorOther();      //должен быть текст ошибки
    }

    @Test
    void nullBalance() {        //перевод при нулевом балансе
        var dashboardPage = new DashboardPage();
        var transferMoney = new TransferPage();
        int money = DataHelper.howManyMoney("1") * 2;
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money, 2);
        dashboardPage.transferSecondToFirst();
        transferMoney.transfer(money, 2);
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

