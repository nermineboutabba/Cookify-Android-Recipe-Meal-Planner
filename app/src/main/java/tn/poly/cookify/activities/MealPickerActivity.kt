package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.poly.cookify.R
import tn.poly.cookify.adapters.RecipeAdapter
import tn.poly.cookify.api.RetrofitClient
import tn.poly.cookify.models.RecipeResponse

class MealPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_picker)

        val recycler = findViewById<RecyclerView>(R.id.recyclerPicker)
        recycler.layoutManager = LinearLayoutManager(this)

        val resultIntent = Intent()

        val adapter = RecipeAdapter(emptyList()) { recipe ->
            resultIntent.putExtra("mealId", recipe.idMeal)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        recycler.adapter = adapter

        RetrofitClient.instance.searchMeals("chicken")
            .enqueue(object : Callback<RecipeResponse> {

                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    adapter.updateData(response.body()?.meals ?: emptyList())
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {}
            })
    }
}