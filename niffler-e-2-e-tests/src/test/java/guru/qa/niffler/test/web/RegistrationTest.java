package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();
  private final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
  private final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";

  @Test
  void shouldRegisterNewUser() {
    final String username = randomUsername();
    final String password = randomPassword();

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
    final String password = randomPassword();

    open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount().setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(password)
        .submitRegistration()
        .formErrorShouldHaveText("Username `" + username + "` already exists");
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    final String username = randomUsername();
    final String password = randomPassword();
    final String submitPassword = randomPassword();

    open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount()
        .setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(submitPassword)
        .submitRegistration()
        .formErrorShouldHaveText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
  }
}
