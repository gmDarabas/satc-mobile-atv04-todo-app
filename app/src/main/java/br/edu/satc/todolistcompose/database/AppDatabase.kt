package br.edu.satc.todolistcompose.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.satc.todolistcompose.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun taskDao() : TaskDao
}