package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();
  private static Faker faker = new Faker();

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    String categoryName = faker.food().ingredient();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(anno -> {

          CategoryJson category = new CategoryJson(
              null,
              categoryName,
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
  public void afterTestExecution(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(anno -> {

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

        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
