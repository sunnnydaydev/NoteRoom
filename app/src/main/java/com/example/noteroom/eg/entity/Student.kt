package com.example.noteroom.eg.entity
import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Create by SunnyDay /08/28 21:29:37
 */
@Entity(ignoredColumns = ["type"])
data class Student(
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    val age: Int
):Person (){
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @Ignore
    var view:View?=null
}
