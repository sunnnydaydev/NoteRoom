# CURD

通过上一节的了解也基本算是入门了，本章节继续巩固基础，了解更多的基础知识点。顺便解决下上节的delete、update的坑~

###### 实体定义需要注意点

```kotlin
@Entity
data class User(
    val name: String,
    val sex: String,
    val age: Int) {
    @PrimaryKey(autoGenerate = true)
    var id = 0L
}
```

（1）表结构

- Room中每个实体都用@Entity标记，Room会为这个实体在数据库中生成对应的表。

- 我们操作表时会通过insert插入实体对象， 每个对象都表示相应表中的一行数据。

- 实体中会定义一些字段，每个字段就是表中的列。

（2）默认行为

```kotlin
/**
 * Create by SunnyDay /08/28 21:29:37
 * @ColumnInfo 注解可修改列名、字段类型
 */
@Entity
data class Student(
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    val age: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
```

- Room 默认将类名称用作数据库表名称。可以使用@Entity(tableName = "xxx")自行更改
- Room 默认使用字段名称作为数据库中的列名称。可以使用@ColumnInfo(name = "xxx")自行更改
- SQLite 中的表和列名称不区分大小写。

（3）主键

- 每个 Room 实体都必须定义一个主键，用于唯一标识相应数据库表中的每一行
- 如果需要通过多个列的组合对实体实例进行唯一标识，则可以通过@Entity#primaryKeys 定义一个复合主键

```kotlin
@Entity(primaryKeys = ["id","name"])
data class Student(
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    val age: Int
)
```

(4)忽略字段

默认情况下，Room 会为实体中定义的每个字段创建一个列。若不想为某个字段生成列则可使用@Ignore

```kotlin
@Entity
data class Student(
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    val age: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @Ignore
    var view:View?=null
}
```
继承了父类的字段则使用@Entity#ignoredColumns属性

```kotlin
open class Person {
    var type = "person"
}
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
```

###### add

###### delete

###### query

###### update
