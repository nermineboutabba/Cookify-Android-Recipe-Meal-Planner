package tn.poly.cookify.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.poly.cookify.R
import tn.poly.cookify.adapters.RecipeAdapter
import tn.poly.cookify.api.RetrofitClient
import tn.poly.cookify.models.RecipeResponse
import tn.poly.cookify.utils.NotificationHelper
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var searchInput: EditText

    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission()
        val helper = NotificationHelper(this)
        helper.createChannel()
        scheduleNotification()

        recyclerView = findViewById(R.id.recyclerView)
        searchInput = findViewById(R.id.searchInput)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupBottomNav(R.id.nav_home)

        adapter = RecipeAdapter(emptyList()) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("id", recipe.idMeal)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()

                if (query.length > 2) {
                    searchRecipes(query)
                } else if (query.isEmpty()) {
                    fetchRecipes()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fetchRecipes()
    }

    private fun fetchRecipes() {
        RetrofitClient.instance.searchMeals("chicken")
            .enqueue(object : Callback<RecipeResponse> {

                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    if (response.isSuccessful) {
                        val meals = response.body()?.meals ?: emptyList()
                        adapter.updateData(meals)
                    }
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun searchRecipes(query: String) {
        RetrofitClient.instance.searchMeals(query)
            .enqueue(object : Callback<RecipeResponse> {

                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    val meals = response.body()?.meals ?: emptyList()
                    adapter.updateData(meals)
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun scheduleNotification() {

        val intent = Intent(this, tn.poly.cookify.receivers.ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val triggerTime = System.currentTimeMillis() + 10000
        // 10 seconds after opening app

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    private fun requestNotificationPermission() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}