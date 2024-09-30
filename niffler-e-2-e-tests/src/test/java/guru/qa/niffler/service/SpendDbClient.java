package guru.qa.niffler.service;


import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }

  public Optional<SpendJson> findSpendById(UUID id) {
    return Optional.of(
        SpendJson.fromEntity(spendDao.findById(id)
            .get()
        )
    );
  }

  public List<SpendJson> findAllSpendsByUsername(String username) {

    List<SpendEntity> spendEntityList = spendDao.findAllByUsername(username);
    List<SpendJson> spendJsonList = new ArrayList<>();
    for (SpendEntity spend : spendEntityList) {
      spendJsonList.add(SpendJson.fromEntity(spend));
    }
    return spendJsonList;
  }

  public void deleteSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    spendDao.delete(spendEntity);
  }

  public CategoryJson createCategory(CategoryJson category) {
    return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category)));
  }

  public Optional<CategoryJson> findCategoryById(UUID id) {
    return Optional.of(CategoryJson.fromEntity(categoryDao.findById(id).get()));
  }

  public Optional<CategoryJson> findByUsernameAndCategoryName(String username, String categoryName) {
    return Optional.of(
        CategoryJson.fromEntity(
            categoryDao.findByUsernameAndCategoryName(username, categoryName)
                .get()
        )
    );
  }

  public void deleteCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    categoryDao.delete(categoryEntity);
  }
}