package br.edu.satc.todolistcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.room.Room
import br.edu.satc.todolistcompose.database.AppDatabase
import br.edu.satc.todolistcompose.ui.screens.HomeScreen
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences(
            "BaseToDoListCompose", Context.MODE_PRIVATE
        )

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "br.edu.satc.contatosapp"
        )
            .allowMainThreadQueries() // allows in MainActivity
            .build()

        val taskDao = db.taskDao()

        setContent {
            val defaultDarkTheme = isSystemInDarkTheme();
            var darkTheme by remember {
                mutableStateOf(sharedPref.getBoolean("DARK_MODE", defaultDarkTheme))
            }

            val onDarkModeChanged: (Boolean) -> Unit = { newDarkMode ->
                darkTheme = newDarkMode
                with(sharedPref.edit()) {
                    putBoolean("DARK_MODE", newDarkMode)
                    apply()
                }
            }

            ToDoListComposeTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var tasksState by remember {
                        mutableStateOf(taskDao.getAll())
                    }

                    HomeScreen(
                        darkMode = darkTheme,
                        onDarkModeChanged = onDarkModeChanged,
                        tasks = tasksState,
                        onNewTaskCreated = {
                            taskDao.insertAll(it)
                            tasksState = taskDao.getAll()
                        },
                        onCompleteTask = { task, complete ->
                            run {
                                task.complete = complete
                                taskDao.updateAll(task)
                            }
                        }
                    )
                }
            }
        }
    }
}
