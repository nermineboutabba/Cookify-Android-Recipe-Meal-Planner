package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import tn.poly.cookify.R
import tn.poly.cookify.database.AppDatabase
import tn.poly.cookify.models.MealPlan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.poly.cookify.api.RetrofitClient
import tn.poly.cookify.database.MealPlanDao
import tn.poly.cookify.models.RecipeResponse

class DayPlanActivity : BaseActivity() {

    private lateinit var breakfastCard: ImageView
    private lateinit var breakfastName: TextView

    private lateinit var lunchCard: ImageView
    private lateinit var lunchName: TextView

    private lateinit var dinnerCard: ImageView
    private lateinit var dinnerName: TextView

    private lateinit var notes: EditText
    private lateinit var saveBtn: Button
    private lateinit var clearBtn: Button
    private lateinit var title: TextView

    private lateinit var date: String

    private var selectedBreakfast: String? = null
    private var selectedLunch: String? = null
    private var selectedDinner: String? = null

    private var currentTarget: String = ""

    private val pickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val mealId = data.getStringExtra("mealId") ?: return@registerForActivityResult

                when (currentTarget) {
                    "breakfast" -> {
                        selectedBreakfast = mealId
                        loadMeal(mealId, breakfastName, breakfastCard)
                    }
                    "lunch" -> {
                        selectedLunch = mealId
                        loadMeal(mealId, lunchName, lunchCard)
                    }
                    "dinner" -> {
                        selectedDinner = mealId
                        loadMeal(mealId, dinnerName, dinnerCard)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plan)

        date = intent.getStringExtra("date") ?: run {
            finish()
            return
        }

        title = findViewById(R.id.dateTitle)
        title.text = "Plan for $date"

        breakfastCard = findViewById(R.id.breakfastImage)
        breakfastName = findViewById(R.id.breakfastName)

        lunchCard = findViewById(R.id.lunchImage)
        lunchName = findViewById(R.id.lunchName)

        dinnerCard = findViewById(R.id.dinnerImage)
        dinnerName = findViewById(R.id.dinnerName)

        notes = findViewById(R.id.notesInput)
        saveBtn = findViewById(R.id.saveBtn)
        clearBtn = findViewById(R.id.clearBtn)

        val dao = AppDatabase.getDatabase(this).mealPlanDao()

        loadExistingPlan(dao)

        // CLICK ON CARD (NOT BUTTONS)
        breakfastCard.setOnClickListener {
            currentTarget = "breakfast"
            openPicker()
        }

        lunchCard.setOnClickListener {
            currentTarget = "lunch"
            openPicker()
        }

        dinnerCard.setOnClickListener {
            currentTarget = "dinner"
            openPicker()
        }

        saveBtn.setOnClickListener {
            Thread {
                dao.insert(
                    MealPlan(
                        date = date,
                        breakfastId = selectedBreakfast,
                        lunchId = selectedLunch,
                        dinnerId = selectedDinner,
                        notes = notes.text.toString()
                    )
                )

                runOnUiThread {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }

        clearBtn.setOnClickListener {
            Thread {
                dao.insert(
                    MealPlan(date, null, null, null, "")
                )

                runOnUiThread {
                    selectedBreakfast = null
                    selectedLunch = null
                    selectedDinner = null
                    notes.setText("")
                    Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
    }

    private fun openPicker() {
        val intent = Intent(this, MealPickerActivity::class.java)
        pickerLauncher.launch(intent)
    }

    private fun loadExistingPlan(dao: MealPlanDao) {
        Thread {
            val existing = dao.getPlan(date)

            runOnUiThread {
                if (existing != null) {

                    selectedBreakfast = existing.breakfastId
                    selectedLunch = existing.lunchId
                    selectedDinner = existing.dinnerId

                    notes.setText(existing.notes)

                    existing.breakfastId?.let {
                        loadMeal(it, breakfastName, breakfastCard)
                    }
                    existing.lunchId?.let {
                        loadMeal(it, lunchName, lunchCard)
                    }
                    existing.dinnerId?.let {
                        loadMeal(it, dinnerName, dinnerCard)
                    }
                }
            }
        }.start()
    }

    private fun loadMeal(id: String, nameView: TextView, imageView: ImageView) {
        RetrofitClient.instance.getMealById(id)
            .enqueue(object : Callback<RecipeResponse> {

                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    val meal = response.body()?.meals?.firstOrNull() ?: return

                    nameView.text = meal.strMeal
                    Glide.with(this@DayPlanActivity)
                        .load(meal.strMealThumb)
                        .into(imageView)
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {}
            })
    }
}