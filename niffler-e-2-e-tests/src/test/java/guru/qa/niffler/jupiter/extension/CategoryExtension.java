package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();
  private static Faker faker = new Faker();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final String categoryName = faker.food().ingredient();
    final String categoryNameTest = faker.animal().name();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class).ifPresent(anno -> {

      CategoryJson category = new CategoryJson(
          null,
          anno.username().equals("test") ? categoryNameTest : categoryName,
          anno.username(),
          anno.archived()

      );

      CategoryJson createdCategory = spendApiClient.addCategory(category);
      if (anno.archived()) {
        CategoryJson archivedCategory = new CategoryJson(
            createdCategory.id(),
            createdCategory.name(),
            createdCategory.username(),
            true
        );
        createdCategory = spendApiClient.updateCategory(archivedCategory);
      }

      context.getStore(NAMESPACE).put(
          context.getUniqueId(),
          createdCategory
      );
    });
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

    if (!category.archived()) {
      CategoryJson archivedCategory = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );

      spendApiClient.updateCategory(archivedCategory);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
