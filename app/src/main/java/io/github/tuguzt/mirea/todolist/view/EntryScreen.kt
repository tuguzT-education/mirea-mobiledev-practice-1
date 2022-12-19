package io.github.tuguzt.mirea.todolist.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.tuguzt.mirea.todolist.view.project.ProjectScreen
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenViewModel

@Composable
fun EntryScreen(
    viewModel: MainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "main") {
            composable(route = "main") {
                MainScreen(
                    todoProjects = viewModel.todoProjects(),
                    completedProjects = viewModel.completedProjects(),
                    onProjectClick = { project ->
                        navController.navigate(route = "project/${project.id}")
                    },
                    onAddNewProjectClick = {
                        // TODO navigate to "add new project" screen
                    },
                )
            }
            composable(route = "project/{id}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("id")
                val project = viewModel.allProjects().find { project -> project.id == projectId }
                    ?: error("Project with id $projectId not present")
                ProjectScreen(
                    project = project,
                    onAddNewTask = {
                        // TODO add new task
                    },
                    onNavigateUp = navController::navigateUp,
                )
            }
        }
    }
}
