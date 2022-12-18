package io.github.tuguzt.mirea.todolist.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.view.navigation.Home
import io.github.tuguzt.mirea.todolist.view.navigation.MainScreenDestination
import io.github.tuguzt.mirea.todolist.view.navigation.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val home = Home()
    val settings = Settings()
    SetupMaterial3RichText {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                MainNavigationBar(
                    destinations = listOf(home, settings),
                    navController = navController,
                )
            },
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = home.route,
            ) {
                composable(home.route) {}
                composable(settings.route) {}
            }
        }
    }
}

@Composable
private fun MainNavigationBar(
    destinations: List<MainScreenDestination> = listOf(Home(), Settings()),
    navController: NavController = rememberNavController(),
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        destinations.forEach { dest ->
            NavigationBarItem(
                icon = { Icon(dest.icon, contentDescription = null) },
                label = { Text(dest.title) },
                selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true,
                onClick = {
                    navController.navigate(dest.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
