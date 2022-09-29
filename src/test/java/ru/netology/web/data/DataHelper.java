package ru.netology.web.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import lombok.Value;
import ru.netology.web.page.DashboardPage;

import static io.restassured.RestAssured.given;

public class DataHelper {
  private DataHelper() {}

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }


  @Value
  public static class VerificationCode {
    private String code;
  }

  public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
    return new VerificationCode("12345");
  }

  @Value
  public static class NumberCard{
    private String numberCard;
  }

  @Value
  public static class CardInfo {
    String number;
  }

  public static CardInfo getCardInfo (String id) {
    if (id.equals("1")) {
      return new CardInfo("5559 0000 0000 0001");
    }
    if (id.equals("2")) {
      return new CardInfo("5559 0000 0000 0002");
    } else {
      return new CardInfo("");
    }
  }
//  public static NumberCard getNumberCard1() {
//    return new NumberCard("5559 0000 0000 0001");
//  }
//
//  public static NumberCard getNumberCard2() {
//    return new NumberCard("5559 0000 0000 0002");
//  }

  public static int howManyMoney(String id) {
    var dashboardPage = new DashboardPage();
    int money = 0;
    if (id.equals("1"))  {
      money = dashboardPage.getCardBalance("2") / 2;
    } else {
      money = dashboardPage.getCardBalance("1") / 2;
    }
    return money;
  }

}
