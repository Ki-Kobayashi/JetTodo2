package com.example.jettodo2.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jettodo2.database.entiry.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // TODO: 監視する必要がなく、ワンショットで処理が完了する非同期 - > suspendを使用
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long // TODO: 追加したIDが返却される

    // 一番最新のものが最初にくる順
    @Query("SELECT * FROM task order by updatedAt desc")
    // TODO: DBで値があるたびに取得したい：監視したい -> Flowを使用
    fun getAllTask(): Flow<List<TaskEntity>>

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)
}
