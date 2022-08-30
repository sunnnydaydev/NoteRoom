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
