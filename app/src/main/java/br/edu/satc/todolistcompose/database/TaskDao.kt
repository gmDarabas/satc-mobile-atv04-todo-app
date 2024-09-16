package br.edu.satc.todolistcompose.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.satc.todolistcompose.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Update
    fun updateAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)
}