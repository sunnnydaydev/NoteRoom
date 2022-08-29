# CURD

通过上一节的了解也基本算是入门了，本章节继续巩固基础，了解更多的基础知识点。顺便解决下上节的delete、update的坑~

###### 1、实体Entity定义需要注意点

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
###### 2、DAO

DAO 全称为Data Access Object 数据访问对象的意思,DAO 不具有属性，但它们定义了一个或多个方法，可用于与应用数据库中的数据进行交互。

dao 可定义两种与数据库交互的方式：

- 可让您不编写任何 SQL 代码的情况下插入、更新和删除数据库中行的便捷方法。
- 可让您编写自己的 SQL 查询以与数据库进行交互的查询方法。

（1）add

@Insert 方法的每个参数必须是带有 @Entity 注解的实体类的实例或数据实体类实例的集合。

调用 @Insert 方法时，Room 会将每个传递的实体实例插入到相应的数据库表中。

如果 @Insert 方法接收单个参数，则会返回 long 值，这是插入项的新 rowId。如果参数是数组或集合，则该方法应改为返回由 long 值组成的数组或集合，并且每个值都作为其中一个插入项的 rowId。

insert插入冲突策略：

```kotlin
@Insert(onConflict = OnConflictStrategy.REPLACE)
```
- OnConflictStrategy.REPLACE 冲突时替换为新记录。 
- OnConflictStrategy.IGNORE 忽略冲突(不建议使用)。 
- OnConflictStrategy.ROLLBACK 废弃了，使用ABORT替代。 
- OnConflictStrategy.FAIL 废弃了，使用ABORT替代。

（2）update

与 @Insert 方法类似，@Update 方法接受数据实体实例作为参数，更新数据库中的一行或者多行数据。

Room 使用主键将传递的实体实例与数据库中的行进行匹配。如果没有具有相同主键的行，Room 不会进行任何更改。 

@Update 方法可以选择性地返回 int 值，该值指示成功更新的行数。

注意入门篇我们留下的坑一个坑，"update"更新不成功，那么如何解决呢？其实更新时指定下对象的索引即可。首先回顾下代码吧：

```kotlin
        updateUser1.setOnClickListener {
            thread {
                val user = user1.copy(name = "jerry", sex = "boy", age = 20)
                userDao.updateUser(user)
                Logger.d(TAG){"updateUser id:${user.id}"}
            }
        }
// log:   D/EgActivity#LOG: updateUser id:0
```
通过log发现要更新的行id为0，我们id自增，插入一行数据则第一行的id应为1，因此0找不到更新的行，所以指定下要修改的行即可：

```kotlin
 val user = user1.copy(name = "jerry", sex = "boy", age = 20).apply { 
     id = 1
  }
 userDao.updateUser(user)
```

同理删除失败也是如上道理~



（3）delete

Room 使用主键将传递的实体实例与数据库中的行进行匹配。如果没有具有相同主键的行，Room 不会进行任何更改。

@Delete 方法可以选择性地返回 int 值，该值指示成功删除的行数。

（4）query

