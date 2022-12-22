package io.github.tuguzt.mirea.todolist.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import dagger.hilt.android.AndroidEntryPoint
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                SetupMaterial3RichText {
                    EntryScreen()
                }
            }
        }
    }
}
