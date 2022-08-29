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
import kotlin.concurrent.thread

class EgActivity : AppCompatActivity() {
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
                thread {
                    userDao.insert(user1)
                    Logger.d(TAG){ "insert user1" }
                }
            }

            adduser2.setOnClickListener {
                thread {
                    userDao.insert(user2)
                    Logger.d(TAG){ "insert user2" }
                }
            }
            deleteUser1.setOnClickListener {
                thread {
                   // Logger.d(TAG){ "delete:${userDao.deleteUser(user1)}" } // 方法不可用

                   userDao.deleteUser(User("aaa","boy",20).apply {
                       id = 3
                   })

                  //   userDao.deleteUserByName("Tom") // 方法可用

                  //  userDao.deleteUsers(arrayListOf(user1,user2)) // 方法不可用
                }

            }

            queryAll.setOnClickListener {
                thread {
                    val sb = StringBuilder()
                    userDao.queryAllUsers().forEach {
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

        updateUser1.setOnClickListener {
            thread {
                val user = user1.copy(name = "jerry", sex = "boy", age = 20).apply {
                    id = 1
                }
                userDao.updateUser(user)
                Logger.d(TAG){"updateUser id:${user.id}"}
            }
        }

    }
}