package com.example.jettodo2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jettodo2.database.dao.TaskDao
import com.example.jettodo2.database.entiry.TaskEntiry

// TODO:exportSchemaがtrueに設定されていると、Roomはコンパイル時にデータベースのスキーマをJSONファイルにエクスポートします。
//     このファイルは、データベースのスキーマが変更されるたびに更新され、
//         デバッグやテストの際にデータベースがどのように構造されているかを確認できる
@Database(
    entities = [
        TaskEntiry::class,
    ],
    version = 1, exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
