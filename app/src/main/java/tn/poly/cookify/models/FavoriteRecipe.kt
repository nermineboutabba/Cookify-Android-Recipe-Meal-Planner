package tn.poly.cookify.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteRecipe(
    @PrimaryKey val idMeal: String,
    val title: String,
    val image: String
)