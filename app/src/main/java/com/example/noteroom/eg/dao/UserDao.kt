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

    /**
     * 方法使用失败
     * */
    @Delete
    fun deleteUser(user: User): Int

    /**
     * 方法使用失败
     * */
    @Delete
    fun deleteUsers(userList: List<User>)

    /**
     * 根据指定的名字删除某一行信息。
     * 存在疑问：
     * 1、执行sql语句为啥要放在query注解中
     * 2、@Delete注解标记方法为啥执行失败。
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

}