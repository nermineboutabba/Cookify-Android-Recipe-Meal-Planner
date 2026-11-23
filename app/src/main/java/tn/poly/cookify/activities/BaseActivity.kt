package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import tn.poly.cookify.R
import tn.poly.cookify.utils.SessionManager

open class BaseActivity : AppCompatActivity() {

    protected lateinit var bottomNav: BottomNavigationView

    fun setupBottomNav(selectedItem: Int) {

        bottomNav = findViewById(R.id.bottomNav)

        // IMPORTANT: prevent re-trigger loops
        bottomNav.setOnItemSelectedListener(null)

        bottomNav.selectedItemId = selectedItem

        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    if (this !is MainActivity) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    true
                }

                R.id.nav_favorites -> {
                    if (this !is FavoritesActivity) {
                        startActivity(Intent(this, FavoritesActivity::class.java))
                        finish()
                    }
                    true
                }

                R.id.nav_calendar -> {
                    if (this !is CalendarActivity) {
                        startActivity(Intent(this, CalendarActivity::class.java))
                        finish()
                    }
                    true
                }

                R.id.nav_logout -> {

                    val session = SessionManager(this)
                    session.logout()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    true
                }

                else -> false
            }
        }
    }

    override fun setContentView(layoutResID: Int) {

        val baseView = layoutInflater.inflate(R.layout.base_activity, null)

        val container = baseView.findViewById<FrameLayout>(R.id.base_container)

        layoutInflater.inflate(layoutResID, container, true)

        super.setContentView(baseView)

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
    }
}