package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class LoginWebTest {

  private static final Config CFG = Config.getInstance();
  private static final String STATISTICS_TEXT = "Statistics";
  private static final String HISTORY_OF_SPENDING_TEXT = "History of Spendings";
  private static final String FAILED_LOGIN_MESSAGE = "Bad credentials";
  private static Faker faker = new Faker();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    open(CFG.frontUrl(), LoginPage.class)
        .login("Oleg", "12345")
        .statisticsHeaderShouldHaveText(STATISTICS_TEXT)
        .historyOfSpendingHeaderShouldHaveText(HISTORY_OF_SPENDING_TEXT);
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    open(CFG.frontUrl(), LoginPage.class)
        .setUsername("Oleg")
        .setPassword(faker.internet().password(3, 12))
        .formErrorShouldHaveTextAfterClick(FAILED_LOGIN_MESSAGE);
  }
}
