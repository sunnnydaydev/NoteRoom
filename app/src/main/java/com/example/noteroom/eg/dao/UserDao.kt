package com.example.noteroom.eg.dao

import androidx.room.*
import com.example.noteroom.eg.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Create by SunnyDay /08/24 21:49:56
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun queryAllUsers(): List<User>

    @Delete
    fun deleteUser(user: User): Int

    @Delete
    fun deleteUsers(userList: List<User>)

    /**
     * 根据指定的名字删除某一行信息。
     * */
    @Query("delete from User where name = :targetName ")
    fun deleteUserByName(targetName: String): Int

    /**
     * 注意：
     * age为数据库中列字段名，也即User表中定义的字段
     * :age代表取值，取方法内传递来的参数。
     * */
    @Query("select * from User where age > :age ")
    fun queryUserOlderThan(age: Int): List<User>

    // for test
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg users: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun loadUserById(id: Int): Flow<User>
}