package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.springframework.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson getSpend(String id, String username){
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id, username).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }

  public List<SpendJson> getSpends(String username,
                                   CurrencyValues filterCurrency,//@Query("filterCurrency") ?
                                   String from,
                                   String to){
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getSpends(username, filterCurrency, from, to).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }

  public SpendJson addSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.CREATED.value(), response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend){
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }

  public void deleteSpends(String username, List<String> ids){
    final Response response;
    try {
      response = spendApi.deleteSpends(username, ids).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.ACCEPTED.value(), response.code());
  }

//  CategoriesController ("/internal/categories")

  public List<CategoryJson> getCategories(String username, boolean excludeArchived){

    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(username, excludeArchived).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }

  public CategoryJson addCategory(CategoryJson category){

    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson category){

    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category).execute();
    } catch (IOException exception) {
      throw new AssertionError(exception);
    }
    assertEquals(HttpStatus.OK.value(), response.code());
    return response.body();
  }
}