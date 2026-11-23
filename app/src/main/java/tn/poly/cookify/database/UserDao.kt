package tn.poly.cookify.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import tn.poly.cookify.models.User

@Dao
interface UserDao {

    @Insert
    fun register(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    fun checkEmail(email: String): User?
}