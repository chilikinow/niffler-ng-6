package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

//  SpendController ("/internal/spends")

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id,
                           @Query("username") String username);

  @GET("/internal/spends/all")
  Call<List<SpendJson>> getSpends(@Query("username") String username,
                                   @Query("currency") CurrencyValues filterCurrency,//@Query("filterCurrency") ?
                                   @Query("from") String from,
                                   @Query("to") String to);

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("/internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @DELETE("/internal/spends/remove")
  Call<Void> deleteSpends(
      @Query("username") String username,
      @Query("ids") List<String> ids
  );

//  CategoriesController ("/internal/categories")

  @GET("/internal/categories/all")
  Call<List<CategoryJson>> getCategories(
      @Query("username") String username,
      @Query("archived") boolean excludeArchived //@Query("excludeArchived") ?
  );

  @POST("/internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("/internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

}