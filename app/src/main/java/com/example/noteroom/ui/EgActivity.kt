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
import com.example.noteroom.utils.Logger
import kotlinx.android.synthetic.main.activity_eg.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class EgActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    companion object {
        const val TAG = "EgActivity#LOG"
        fun open(context: Context) {
            context.startActivity(Intent(context, EgActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eg)

        val userDao = AppDataBase.getDataBase(applicationContext).userDao()
        val user1 = User("Tom", "boy", 18)
        val user2 = User("Kate", "girl", 18)

        adduser1.setOnClickListener {
            launch {
                Log.d("MyTag","currentThread:${Thread.currentThread().name}") // currentThread:main
                userDao.insertUser(user1)
            }
        }

        adduser2.setOnClickListener {
            launch {
                userDao.insertUser(user2)
            }
        }
        deleteUser1.setOnClickListener {
            launch {
                userDao.deleteUser(user1.apply {
                    id = 1
                })
            }
        }

        updateUser1.setOnClickListener {
            launch {
                userDao.updateUser(user1.copy(name = "updateName", sex = "boy", age = 20).apply {
                    id = 1
                })
            }
        }

        queryAll.setOnClickListener {
            launch {
                val sb = StringBuilder()
                userDao.queryUser().forEach {
                    sb.append("PrimaryKey:${it.id} \n")
                        .append("userName:${it.name} \n")
                        .append("userSex:${it.sex} \n")
                        .append("userAge:${it.age} \n")
                }
                runOnUiThread {
                    dbInfo.text = sb.toString()
                }
            }
        }
    }
}