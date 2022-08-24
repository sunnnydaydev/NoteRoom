package com.example.noteroom.eg.dao

import androidx.room.*
import com.example.noteroom.eg.entity.User

/**
 * Create by SunnyDay /08/24 21:49:56
 */
@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun queryAllUsers(): List<User>

    @Delete
    fun deleteAllUser(user: User)

    /**
     * 注意这里的:age 代表数据库查询结果对象的age
     * */
    @Query("select * from User where age > :age ")
    fun queryUserOlderThan(age: Int): List<User>
}