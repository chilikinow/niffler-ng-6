package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                RandomDataUtils.randomCategoryName(),
                "oleg",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx333",
            "oleg"
        )
    );

    System.out.println(spend);
  }

  @Test
  void springJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson myself = usersDbClient.createUser(
        new UserJson(
            null,
            "sergey",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson friend = usersDbClient.createUser(
        new UserJson(
            null,
            "mariya",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson income = usersDbClient.createUser(
        new UserJson(
            null,
            "valentino",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson outcome = usersDbClient.createUser(
        new UserJson(
            null,
            "ekaterina",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    // Добавляем входящее приглашение
    usersDbClient.addInvitation(income, myself);

    // Добавляем исходящее приглашение
    usersDbClient.addInvitation(myself, outcome);

    // Добавляем друзей
    usersDbClient.addFriends(myself, friend);
  }
}