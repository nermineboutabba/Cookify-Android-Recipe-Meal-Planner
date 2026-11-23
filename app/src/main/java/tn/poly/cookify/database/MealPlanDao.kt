package tn.poly.cookify.database

import androidx.room.*
import tn.poly.cookify.models.MealPlan

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(plan: MealPlan)

    @Query("SELECT * FROM meal_plans WHERE date = :date")
    fun getPlan(date: String): MealPlan?

    @Query("SELECT * FROM meal_plans")
    fun getAllPlans(): List<MealPlan>
}