package tn.poly.cookify.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.poly.cookify.api.RetrofitClient
import tn.poly.cookify.models.Recipe
import tn.poly.cookify.models.RecipeResponse
import tn.poly.cookify.R
import tn.poly.cookify.database.AppDatabase
import tn.poly.cookify.models.FavoriteRecipe
import kotlin.concurrent.thread

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var btnFavorite: Button
    private lateinit var dao: tn.poly.cookify.database.RecipeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val title = findViewById<TextView>(R.id.detailTitle)
        val image = findViewById<ImageView>(R.id.detailImage)
        val instructions = findViewById<TextView>(R.id.detailInstructions)
        val ingredients = findViewById<TextView>(R.id.detailIngredients)
        btnFavorite = findViewById(R.id.btnFavorite)
        val db = AppDatabase.getDatabase(this)
        dao = db.recipeDao()

        val id = intent.getStringExtra("id")

        if (id != null) {
            fetchRecipeDetails(id, title, image, instructions, ingredients)
        }
    }

    private fun fetchRecipeDetails(
        id: String,
        title: TextView,
        image: ImageView,
        instructions: TextView,
        ingredients: TextView
    ) {
        RetrofitClient.instance.getMealById(id)
            .enqueue(object : Callback<RecipeResponse> {

                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    val recipe = response.body()?.meals?.firstOrNull()

                    if (recipe != null) {

                        title.text = recipe.strMeal

                        Glide.with(this@RecipeDetailActivity)
                            .load(recipe.strMealThumb)
                            .into(image)

                        instructions.text = recipe.strInstructions
                        ingredients.text = buildIngredients(recipe)

                        btnFavorite.setOnClickListener {

                            val favorite = FavoriteRecipe(
                                idMeal = recipe.idMeal,
                                title = recipe.strMeal,
                                image = recipe.strMealThumb
                            )

                            thread {
                                dao.insert(favorite)
                            }

                            Toast.makeText(
                                this@RecipeDetailActivity,
                                "Added to favorites",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                    instructions.text = "Failed to load recipe"
                }
            })
    }

    private fun buildIngredients(recipe: Recipe): String {
        val ingredients = mutableListOf<String>()

        val fields = listOf(
            recipe.strIngredient1 to recipe.strMeasure1,
            recipe.strIngredient2 to recipe.strMeasure2,
            recipe.strIngredient3 to recipe.strMeasure3,
            recipe.strIngredient4 to recipe.strMeasure4,
            recipe.strIngredient5 to recipe.strMeasure5,
            recipe.strIngredient6 to recipe.strMeasure6,
            recipe.strIngredient7 to recipe.strMeasure7,
            recipe.strIngredient8 to recipe.strMeasure8,
            recipe.strIngredient9 to recipe.strMeasure9,
            recipe.strIngredient10 to recipe.strMeasure10
        )

        for ((ingredient, measure) in fields) {
            if (!ingredient.isNullOrEmpty()) {
                ingredients.add("$ingredient - ${measure ?: ""}")
            }
        }

        return ingredients.joinToString("\n")
    }
}