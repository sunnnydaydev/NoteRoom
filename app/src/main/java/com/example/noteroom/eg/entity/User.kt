package com.example.noteroom.eg.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by SunnyDay /08/24 21:43:27
 */
@Entity
data class User(
    val name: String,
    val sex: String,
    val age: Int) {
    @PrimaryKey(autoGenerate = true)
    var id = 0L
}