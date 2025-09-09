package com.example.youtubeapp.repository

import com.example.youtubeapp.data.local.UserDao
import com.example.youtubeapp.data.local.entities.User

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Boolean {
        return try {
            userDao.insertUser(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUsers(): List<User> {
        return userDao.getUsers()
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun deleteUserById(uid: Int) {
        userDao.deleteById(uid)
    }

    suspend fun updateUser(user: User) =
        userDao.updateUser(user)
}