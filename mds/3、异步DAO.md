# 异步DAO

Room 不允许在主线程上访问数据库，因此增删改查的操作需要异步处理。Room提供了与不同框架的集成主要分为如下几类：
- Kotlin#协程、Kotlin#Flow
- RxJava
- Guava
- Jetpack#LiveData

我们可根据自己需求来选择。本章节练习下Kotlin#协程、Kotlin#Flow。

如需将 Kotlin Flow 和协程与 Room 搭配使用，必须在 build.gradle 文件中添加 room-ktx 依赖。

```groovy
    // room ktx
    implementation "androidx.room:room-ktx:$room_version"
```

接下来就是增删改查的异步写法了：

```kotlin
@Dao
interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from User")
    suspend fun queryUser(): List<User>
}
```
可见都是挂起函数，然后怎样使用呢？也很简单~

```kotlin
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
```
获取我们刚了解协程时或有如下疑问：

MainScope()是安卓提供的一个方法，对协程进行了封装，其下文为Main线程的上下文，因此通过MainScope开启的线程都是跑在Main线程的，此时
在主线程调用操作数据库的api这应该是不允许的吧，耗时操作呀~

哈哈哈这就要探究下挂起函数了，若是挂起函数的内部还跑在主线程那肯定有问题的呀，让我们看看Room为我们生成的实现类是怎样做的：

```java
public final class UserDao_Impl implements UserDao {
    /**
     * 可见：
     * 1、首先为挂起函数多生成了一个参数Continuation
     * 2、主要的逻辑都在Callable call 方法中实现
     * 3、把参数传给了CoroutinesRoom的execute方法来处理
     * */
    @Override
    public Object insertUser(final User user, final Continuation<? super Unit> p1) {
        return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
            @Override
            public Unit call() throws Exception {
                __db.beginTransaction();
                try {
                    __insertionAdapterOfUser.insert(user);
                    __db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    __db.endTransaction();
                }
            }
        }, p1);
    }
    
}
```
这里我们就猜想了，有Callable、execute。怎么和线程池好像，看来肯定有线程相关的操作~ 继续探究

```kotlin
        @JvmStatic
        public suspend fun <R> execute(
            db: RoomDatabase,
            inTransaction: Boolean,
            callable: Callable<R>
        ): R {
            if (db.isOpen && db.inTransaction()) {
                return callable.call()
            }

            // Use the transaction dispatcher if we are on a transaction coroutine, otherwise
            // use the database dispatchers.
            val context = coroutineContext[TransactionElement]?.transactionDispatcher
                ?: if (inTransaction) db.transactionDispatcher else db.queryDispatcher
            return withContext(context) {
                callable.call()
            }
        }
```
可见有dispatcher的影子，而且通过withContext切线程环境执行回调方法~ 
