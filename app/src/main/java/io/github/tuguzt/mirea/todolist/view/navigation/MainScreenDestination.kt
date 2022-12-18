package io.github.tuguzt.mirea.todolist.view.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import io.github.tuguzt.mirea.todolist.R

sealed class MainScreenDestination(
    override val route: String,
    override val icon: ImageVector,
    override val title: String,
) : IconDestination, TitleDestination {
    class Home(context: Context) : MainScreenDestination(
        route = "home",
        icon = Icons.Rounded.Home,
        title = context.getString(R.string.home),
    )

    class Settings(context: Context) : MainScreenDestination(
        route = "settings",
        icon = Icons.Rounded.Settings,
        title = context.getString(R.string.settings),
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun Home() = MainScreenDestination.Home(LocalContext.current)

@SuppressLint("ComposableNaming")
@Composable
fun Settings() = MainScreenDestination.Settings(LocalContext.current)
