package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

  private final ElementsCollection categoryList = $$(".MuiChip-root");
  private final SelenideElement archiveButton = $x("//button[text()='Archive']");
  private final SelenideElement unarchiveButton = $x("//button[text()='Unarchive']");
  private final SelenideElement successArchiveMessage = $(".MuiAlert-message .MuiTypography-body1");
  private final SelenideElement showArchiveSwitcher = $(".MuiFormControlLabel-root");

  public ProfilePage clickArchiveCategoryByName(String categoryName) {
    categoryList
        .findBy(text(categoryName))
        .parent()
        .$(".MuiIconButton-sizeMedium[aria-label='Archive category']")
        .click();
    return this;
  }

  public ProfilePage clickUnarchiveCategoryByName(String categoryName) {
    categoryList
        .findBy(text(categoryName))
        .parent()
        .$("[data-testid='UnarchiveOutlinedIcon']")
        .click();
    return this;
  }

  public ProfilePage clickShowArchiveCategoryButton() {
    showArchiveSwitcher.click();
    return this;
  }

  public ProfilePage clickArchiveButton() {
    archiveButton.click();
    return this;
  }

  public ProfilePage clickUnarchiveButton() {
    unarchiveButton.click();
    return this;
  }

  public ProfilePage shouldBeVisibleSuccessMessage(String categoryName, String expectedMessage) {
    successArchiveMessage.shouldBe(visible).shouldHave(text("Category " + categoryName + " " + expectedMessage));
    return this;
  }

  public ProfilePage shouldBeVisibleCategory(String categoryName) {
    categoryList.findBy(text(categoryName)).shouldBe(visible);
    return this;
  }

  public ProfilePage shouldNotBeVisibleCategory(String categoryName) {
    categoryList.findBy(text(categoryName)).shouldNotBe(visible);
    return this;
  }
}
