package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {


    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        DashboardPage.balancedBalance();
    }

    @Test
    void transferSecondToFirstCard() {  //перевод со второго счёта на первый
        int money = DashboardPage.howManyMoney(2);
        int expectedBalanceCard1 = DashboardPage.getFirstCardBalance() + money;
        int expectedBalanceCard2 = DashboardPage.getSeconCardBalance() - money;
        DashboardPage.transfer(money, 1,2);
        int actualBalanceCard1 = DashboardPage.getFirstCardBalance();
        int actualBalanceCard2 = DashboardPage.getSeconCardBalance();
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferFirstToSecondCard() {  //перевод с первого счёта на второй
        int money = DashboardPage.howManyMoney(1);
        int expectedBalanceCard1 = DashboardPage.getFirstCardBalance() - money;
        int expectedBalanceCard2 = DashboardPage.getSeconCardBalance() + money;
        DashboardPage.transfer(money, 2,1);
        int actualBalanceCard1 = DashboardPage.getFirstCardBalance();
        int actualBalanceCard2 = DashboardPage.getSeconCardBalance();
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void errorNotAccountNumber() {  //перевод без указания номера счёта
        int money = DashboardPage.howManyMoney(1);
        DashboardPage.transfer(money, 1,0);
        $x("//*[@class=\"notification__content\"]").shouldHave(text("Ошибка! Произошла ошибка"));

    }

    @Test
    void transferMax() {    //перевод всей  суммы
        int money = DashboardPage.howManyMoney(2) * 2;
        int expectedBalanceCard1 = DashboardPage.getFirstCardBalance() + money;
        int expectedBalanceCard2 = DashboardPage.getSeconCardBalance() - money;
        DashboardPage.transfer(money, 1,2);
        int actualBalanceCard1 = DashboardPage.getFirstCardBalance();
        int actualBalanceCard2 = DashboardPage.getSeconCardBalance();
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void reloadData() {     //кнопка обновить
        int expectedBalanceCard1 = DashboardPage.getFirstCardBalance();
        int expectedBalanceCard2 = DashboardPage.getSeconCardBalance();
        DashboardPage.reloadData();
        int actualBalanceCard1 = DashboardPage.getFirstCardBalance();
        int actualBalanceCard2 = DashboardPage.getSeconCardBalance();
        assertEquals(expectedBalanceCard1, actualBalanceCard1);
        assertEquals(expectedBalanceCard2, actualBalanceCard2);
    }

    @Test
    void transferOverMax() {    //перевод большей суммы чем есть на счете
        int money = DashboardPage.howManyMoney(2) * 3;
        DashboardPage.transfer(money, 1,2);
        $x("//*[@]").shouldHave(text(""));     // должен быть текст ошибки
    }

    @Test
    void transferNotMoney() {  // перевод без указания суммы
        DashboardPage.transfer(0,1,2);
        $x("//*[@]").shouldHave(text(""));      //должен быть текст ошибки
    }

    @Test
    void transferSecondToSecondAccountNumbers() {  //перевод на тот же счёт
        int money = DashboardPage.howManyMoney(2);
        DashboardPage.transfer(money, 2,1);
        $x("//*[@]").shouldHave(text(""));      //должен быть текст ошибки
    }

    @Test
    void nullBalance() {        //перевод при нулевом балансе
        int money = DashboardPage.howManyMoney(1) * 2;
        DashboardPage.transfer(money, 2,1);
        DashboardPage.transfer(money, 2,1);
        $x("//*[@]").shouldHave(text(""));      //должен быть текст ошибки
    }
}

