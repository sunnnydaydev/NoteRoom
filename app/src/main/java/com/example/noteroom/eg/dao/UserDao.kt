package com.example.noteroom.eg.dao

import androidx.room.*
import com.example.noteroom.eg.entity.User

/**
 * Create by SunnyDay /08/24 21:49:56
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from User")
    suspend fun queryUser(): List<User>
}