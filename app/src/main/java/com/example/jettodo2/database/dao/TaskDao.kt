package com.example.jettodo2.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jettodo2.database.entiry.TaskEntiry
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // TODO: 監視する必要がなく、ワンショットで処理が完了する非同期 - > suspendを使用
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntiry): Long // TODO: 追加したIDが返却される

    @Query("SELECT * FROM TaskEntiry")
    // TODO: DBで値があるたびに取得したい：監視したい -> Flowを使用
    fun getAllTask(): Flow<List<TaskEntiry>>

    @Delete
    suspend fun deleteTask(task: TaskEntiry)

    @Update
    suspend fun updateTask(task: TaskEntiry)
}
