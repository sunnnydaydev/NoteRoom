package com.example.noteroom.eg.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteroom.eg.dao.UserDao
import com.example.noteroom.eg.entity.User

/**
 * Create by SunnyDay /08/24 21:55:26
 */
@Database(version = 1,entities = [User::class]) // 数据库版本、包含的表
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao // 提供User数据库的访问接口对象，如果有其他表也可以在这定义

    companion object { // 注意数据库对象要进程内单例
        private var instance: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            instance?.let {
                return it
            }

            return Room.databaseBuilder(context, AppDataBase::class.java,"app_database").build().apply { // apply 的用法
                instance = this
            }
        }
    }
}