package com.example.noteroom.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.noteroom.R
import com.example.noteroom.eg.dao.UserDao
import com.example.noteroom.eg.db.AppDataBase
import com.example.noteroom.eg.entity.User
import kotlin.concurrent.thread

class EgActivity : AppCompatActivity() {
    companion object{
        const val TAG = "EgActivity"
        fun open(context: Context){
            context.startActivity(Intent(context,EgActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eg)

        thread {
            // 1、创建数据库
            // 数据库对象最好设计成单例，每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。
            val userDao = AppDataBase.getDataBase(applicationContext).userDao()

            //2、User标中添加一行信息
            val user = User("Tom","boy",18)
            userDao.insert(user) //Caused by: java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
            // 3、查询所有的元素
            userDao.queryAllUsers().forEach {
                Log.d(TAG,"userName:${it.name}")
                Log.d(TAG,"userSex:${it.sex}")
                Log.d(TAG,"userAge:${it.age}")
            }
        }
    }
}