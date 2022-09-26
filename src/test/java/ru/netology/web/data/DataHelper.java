package ru.netology.web.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import lombok.Value;

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

  public static NumberCard getNumberCard1() {
    return new NumberCard("5559 0000 0000 0001");
  }

  public static NumberCard getNumberCard2() {
    return new NumberCard("5559 0000 0000 0002");
  }

}
