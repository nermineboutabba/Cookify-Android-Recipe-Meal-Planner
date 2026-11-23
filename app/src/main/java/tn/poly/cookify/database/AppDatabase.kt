package tn.poly.cookify.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import tn.poly.cookify.models.FavoriteRecipe
import tn.poly.cookify.models.MealPlan
import tn.poly.cookify.models.User
import java.util.Calendar
import java.util.concurrent.Executors

@Database(
    entities = [
        FavoriteRecipe::class,
        MealPlan::class,
        User::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cookify_db"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let { seedDemoData(it) }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private fun dateKey(offsetDays: Int): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, offsetDays)
            return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
        }

        private fun seedDemoData(db: AppDatabase) {
            if (db.userDao().checkEmail("demo@cookify.tn") == null) {
                db.userDao().register(
                    User(
                        username = "Chef Nermine",
                        email = "demo@cookify.tn",
                        password = "Demo1234!"
                    )
                )
            }

            if (db.recipeDao().getAllFavorites().isEmpty()) {
                val favorites = listOf(
                    FavoriteRecipe(
                        idMeal = "52772",
                        title = "Teriyaki Chicken Casserole",
                        image = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg"
                    ),
                    FavoriteRecipe(
                        idMeal = "52795",
                        title = "Chicken Handi",
                        image = "https://www.themealdb.com/images/media/meals/wyxwsp1486979827.jpg"
                    ),
                    FavoriteRecipe(
                        idMeal = "52844",
                        title = "Lasagne",
                        image = "https://www.themealdb.com/images/media/meals/wtsvxx1511296896.jpg"
                    ),
                    FavoriteRecipe(
                        idMeal = "52819",
                        title = "Cajun Spiced Fish Tacos",
                        image = "https://www.themealdb.com/images/media/meals/uvuyxu1503067369.jpg"
                    )
                )
                favorites.forEach { db.recipeDao().insert(it) }
            }

            if (db.mealPlanDao().getAllPlans().isEmpty()) {
                db.mealPlanDao().insert(
                    MealPlan(
                        date = dateKey(0),
                        breakfastId = "52819",
                        lunchId = "52772",
                        dinnerId = "52844",
                        notes = "High-protein Mediterranean prep day"
                    )
                )
                db.mealPlanDao().insert(
                    MealPlan(
                        date = dateKey(1),
                        breakfastId = null,
                        lunchId = "52795",
                        dinnerId = "52819",
                        notes = "Light dinner menu"
                    )
                )
                db.mealPlanDao().insert(
                    MealPlan(
                        date = dateKey(3),
                        breakfastId = "52772",
                        lunchId = "52844",
                        dinnerId = "52795",
                        notes = "Weekend family meal plan"
                    )
                )
            }
        }
    }
}
