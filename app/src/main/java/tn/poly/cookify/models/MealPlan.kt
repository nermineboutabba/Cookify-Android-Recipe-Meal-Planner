package tn.poly.cookify.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlan(
    @PrimaryKey val date: String,

    val breakfastId: String?,
    val lunchId: String?,
    val dinnerId: String?,

    val notes: String?
)