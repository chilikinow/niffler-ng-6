package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsTest {
  private static final Config CFG = Config.getInstance();

  @Test
  void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .goToFriends()
        .shouldHaveMyFriendsListHeader("My friends")
        .shouldBePresentInFriendsTable(user.friend());
  }

  @Test
  void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .goToFriends()
        .shouldHaveEmptyFriendsTable("There are no users yet");
  }

  @Test
  void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .goToFriends()
        .shouldFriendRequestList("Friend requests")
        .shouldBePresentInRequestsTable(user.income());
  }

  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .goToAllPeople()
        .shouldBePresentInAllPeopleTableAndCheckStatus(user.outcome(), "Waiting...");
  }
}