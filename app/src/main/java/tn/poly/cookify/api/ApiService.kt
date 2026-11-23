package tn.poly.cookify.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tn.poly.cookify.models.RecipeResponse

interface ApiService {

    @GET("search.php")
    fun searchMeals(
        @Query("s") query: String
    ): Call<RecipeResponse>

    @GET("lookup.php")
    fun getMealById(
        @Query("i") id: String
    ): Call<RecipeResponse>
}