package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
//  private final SpendApiClient spendApiClient = new SpendApiClient();
private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.categories())) {
            Category categoryAnno = userAnno.categories()[0];
            CategoryJson category = new CategoryJson(
                null,
                randomCategoryName(),
                userAnno.username(),
                categoryAnno.archived()
            );
            // Отправляем запрос на создание категории
//            CategoryJson created = spendApiClient.createCategory(category);
            CategoryJson created = spendDbClient.createCategory(category);

//            // Если категория должна быть архивной, отправляем второй запрос на обновление
//            if (categoryAnno.archived()) {
//              CategoryJson archivedCategory = new CategoryJson(
//                  created.id(),
//                  created.name(),
//                  created.username(),
//                  true
//              );
//              created = spendApiClient.updateCategory(archivedCategory);
//            }

            // Сохраняем уже созданную или обновленную категорию в контекст
            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                created
            );
          }
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

//  // Если категория существует и не архивирована, архивируем её после теста
//    if (category != null && !category.archived()) {
//      category = new CategoryJson(
//          category.id(),
//          category.name(),
//          category.username(),
//          true
//      );
//      spendApiClient.updateCategory(category);
//    }

    // Если категория существует удаляем ее после теста
    if (category != null) {
      spendDbClient.deleteCategory(category);
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
