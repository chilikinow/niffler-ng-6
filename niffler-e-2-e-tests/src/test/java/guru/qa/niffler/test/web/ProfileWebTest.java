package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class ProfileWebTest {

  private static final Config CFG = Config.getInstance();

  @Category(
      username = "Oleg"
  )
  @Test
  void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {
    open(CFG.frontUrl(), LoginPage.class)
        .login("Oleg", "12345")
        .getTopMenu()
        .goToProfile()
        .clickArchiveCategoryByName(category.name())
        .clickArchiveButton()
        .shouldBeVisibleSuccessMessage(category.name(), "is archived")
        .shouldNotBeVisibleCategory(category.name());
  }

  @Category(
      username = "Oleg",
      archived = true
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    open(CFG.frontUrl(), LoginPage.class)
        .login("Oleg", "12345")
        .getTopMenu()
        .goToProfile()
        .clickShowArchiveCategoryButton()
        .clickUnarchiveCategoryByName(category.name())
        .clickUnarchiveButton()
        .shouldBeVisibleSuccessMessage(category.name(), "is unarchived")
        .clickShowArchiveCategoryButton()
        .shouldBeVisibleCategory(category.name());
  }
}