package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.poly.cookify.R
import tn.poly.cookify.database.AppDatabase
import tn.poly.cookify.utils.SessionManager
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var goToSignup: TextView

    override fun onStart() {
        super.onStart()

        val session = SessionManager(this)

        if (session.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginBtn = findViewById(R.id.loginBtn)
        goToSignup = findViewById(R.id.goToSignup)
        val demoLoginBtn: Button = findViewById(R.id.demoLoginBtn)

        val db = AppDatabase.getDatabase(this)

        // Ensure initial seed runs before sign-in
        thread { db.userDao().checkEmail("demo@cookify.tn") }

        demoLoginBtn.setOnClickListener {
            emailInput.setText("demo@cookify.tn")
            passwordInput.setText("Demo1234!")
            loginBtn.performClick()
        }

        // Login handler
        loginBtn.setOnClickListener {

            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {
                val user = db.userDao().login(email, password)

                runOnUiThread {
                    if (user != null) {

                        val session = SessionManager(this)
                        session.saveLogin(user.id)

                        Toast.makeText(this, "Welcome ${user.username}", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // GO TO SIGNUP
        goToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}