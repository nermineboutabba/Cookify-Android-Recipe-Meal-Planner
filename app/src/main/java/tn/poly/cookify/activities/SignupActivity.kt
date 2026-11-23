package tn.poly.cookify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.poly.cookify.R
import tn.poly.cookify.database.AppDatabase
import tn.poly.cookify.models.User
import kotlin.concurrent.thread

class SignupActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signup)

        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signupBtn = findViewById(R.id.signupBtn)

        val db = AppDatabase.getDatabase(this)

        signupBtn.setOnClickListener {

            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {

                val existing = db.userDao().checkEmail(email)

                runOnUiThread {
                    if (existing != null) {
                        Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                    } else {

                        thread {
                            db.userDao().register(
                                User(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                            )

                            runOnUiThread {
                                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}