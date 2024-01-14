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
    // TODO: 監視する必要がなく、ワンショットで処理が完了する非同期（一回だけ取得できればいい） - > suspendを使用
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long // TODO: 追加したIDが返却される

    // 一番最新のものが最初にくる順
    @Query("SELECT * FROM task order by updatedAt desc")
    // TODO: DBで値があるたびに取得したい：監視したい -> Flowを使用
    fun getAllTask(): Flow<List<TaskEntity>>

    // TODO: Roomでは「:変数名」を使うことで、引数の変数をSQLに埋め込める
    @Query("SELECT * FROM task WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)
}
