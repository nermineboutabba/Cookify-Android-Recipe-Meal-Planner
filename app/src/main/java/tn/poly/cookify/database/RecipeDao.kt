package tn.poly.cookify.database

import androidx.room.*
import tn.poly.cookify.models.FavoriteRecipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: FavoriteRecipe)

    @Delete
    fun delete(recipe: FavoriteRecipe)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): List<FavoriteRecipe>
}