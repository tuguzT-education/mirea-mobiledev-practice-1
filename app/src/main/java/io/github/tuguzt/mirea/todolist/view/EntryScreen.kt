package io.github.tuguzt.mirea.todolist.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.tuguzt.mirea.todolist.view.project.ProjectScreen
import io.github.tuguzt.mirea.todolist.view.task.TaskScreen
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.project.ProjectViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.task.TaskViewModel

@Composable
fun EntryScreen(
    viewModel: MainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "main") {
            composable(route = "main") {
                MainScreen(
                    state = viewModel.state,
                    onProjectClick = { project ->
                        navController.navigate(route = "project/${project.id}")
                    },
                    onAddNewProjectClick = {
                        // TODO add new project
                    },
                )
            }
            composable(route = "project/{id}") { backStackEntry ->
                val projectViewModel: ProjectViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("id"))
                projectViewModel.setup(projectId)

                ProjectScreen(
                    project = projectViewModel.state.project,
                    onAddNewTask = {
                        // TODO add new task
                    },
                    onTaskClick = { task ->
                        val project = projectViewModel.state.project
                        navController.navigate(route = "project/${project.id}/task/${task.id}")
                    },
                    onNavigateUp = navController::navigateUp,
                )
            }
            composable(route = "project/{projectId}/task/{taskId}") { backStackEntry ->
                val taskViewModel: TaskViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("projectId"))
                val taskId = checkNotNull(backStackEntry.arguments?.getString("taskId"))
                taskViewModel.setup(projectId, taskId)

                TaskScreen(
                    task = taskViewModel.state.task,
                    onTaskCompletion = {
                        // TODO close or reopen task
                    },
                    onNavigateUp = navController::navigateUp,
                )
            }
        }
    }
}
