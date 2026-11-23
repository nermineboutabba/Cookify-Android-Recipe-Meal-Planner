package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import tn.poly.cookify.R
import tn.poly.cookify.database.AppDatabase
import java.util.Calendar

class CalendarActivity : BaseActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        db = AppDatabase.getDatabase(this)

        setupBottomNav(R.id.nav_calendar)

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

                val calendar = eventDay.calendar

                val selectedDate =
                    "${calendar.get(Calendar.YEAR)}-" +
                            "${calendar.get(Calendar.MONTH) + 1}-" +
                            "${calendar.get(Calendar.DAY_OF_MONTH)}"

                val intent = Intent(this@CalendarActivity, DayPlanActivity::class.java)
                intent.putExtra("date", selectedDate)
                startActivity(intent)
            }
        })
    }

    private fun loadCalendarData() {
        Thread {
            val plans = db.mealPlanDao().getAllPlans()
            val events = mutableListOf<EventDay>()

            for (plan in plans) {

                val parts = plan.date.split("-")
                val cal = Calendar.getInstance()
                cal.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())

                val count = listOf(
                    plan.breakfastId,
                    plan.lunchId,
                    plan.dinnerId
                ).count { !it.isNullOrEmpty() }

                val drawable = when (count) {
                    0 -> R.drawable.day_empty
                    1, 2 -> R.drawable.day_partial
                    else -> R.drawable.day_full
                }

                events.add(EventDay(cal, drawable))
            }

            runOnUiThread {
                calendarView.setEvents(events)
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        loadCalendarData()
    }
}