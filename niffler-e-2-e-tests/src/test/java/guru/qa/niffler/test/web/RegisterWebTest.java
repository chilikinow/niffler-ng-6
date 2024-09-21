package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class RegisterWebTest {

  private static final Config CFG = Config.getInstance();
  private final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
  private final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "Username `Oleg` already exists";
  private final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";
  private static Faker faker = new Faker();

  @Test
  void shouldRegisterNewUser() {
    final String username = faker.name().username();
    final String password = faker.internet().password(3, 12);

    open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount()
        .setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(password)
        .submitRegistration()
        .successRegisterMessageShouldHaveText(EXPECTED_REGISTRATION_MESSAGE);
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername() {
    final String username = "Oleg";
    final String password = faker.internet().password(3, 12);

    open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount().setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(password)
        .submitRegistration()
        .formErrorShouldHaveText(USERNAME_ALREADY_EXISTS_ERROR_TEXT);
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount()
        .setUsername(faker.name().username())
        .setPassword(faker.internet().password(3, 12))
        .setPasswordSubmit(faker.internet().password(3, 12))
        .submitRegistration()
        .formErrorShouldHaveText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
  }
}
