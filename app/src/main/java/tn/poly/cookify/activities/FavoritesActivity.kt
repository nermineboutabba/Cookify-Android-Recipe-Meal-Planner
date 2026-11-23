package tn.poly.cookify.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.poly.cookify.R
import tn.poly.cookify.adapters.FavoriteAdapter
import tn.poly.cookify.database.AppDatabase
import tn.poly.cookify.database.RecipeDao
import tn.poly.cookify.models.FavoriteRecipe
import kotlin.concurrent.thread

class FavoritesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dao: RecipeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.recyclerFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupBottomNav(R.id.nav_favorites)

        val db = AppDatabase.getDatabase(this)
        dao = db.recipeDao()

        loadFavorites()
    }

    private fun loadFavorites() {
        thread {
            val favorites = dao.getAllFavorites()

            runOnUiThread {
                val adapter = FavoriteAdapter(favorites) { item ->
                    deleteFavorite(item)
                }

                recyclerView.adapter = adapter
            }
        }
    }

    private fun deleteFavorite(item: FavoriteRecipe) {
        thread {
            dao.delete(item)

            val updated = dao.getAllFavorites()

            runOnUiThread {
                recyclerView.adapter = FavoriteAdapter(updated) { newItem ->
                    deleteFavorite(newItem)
                }
            }
        }
    }
}