package io.github.tuguzt.mirea.todolist.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import io.github.tuguzt.mirea.todolist.view.project.AddNewProjectScreen
import io.github.tuguzt.mirea.todolist.view.project.ProjectScreen
import io.github.tuguzt.mirea.todolist.view.task.AddNewTaskScreen
import io.github.tuguzt.mirea.todolist.view.task.TaskScreen
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.project.AddNewProjectViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.project.ProjectViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.task.AddNewTaskViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.task.TaskViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EntryScreen(
    mainViewModel: MainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "main") {
            composable(route = "main") {
                MainScreen(
                    state = mainViewModel.state,
                    onProjectClick = { project ->
                        navController.navigate(route = "project/${project.id}")
                    },
                    onAddNewProjectClick = {
                        navController.navigate(route = "addNewProject")
                    },
                )
            }
            composable(route = "project/{id}") { backStackEntry ->
                val viewModel: ProjectViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("id"))
                viewModel.setup(projectId)

                ProjectScreen(
                    project = viewModel.state.project,
                    onAddNewTask = {
                        navController.navigate(route = "addNewTask")
                    },
                    onTaskClick = { task ->
                        val project = viewModel.state.project
                        navController.navigate(route = "project/${project.id}/task/${task.id}")
                    },
                    onTaskDueClick = {
                        // TODO change due of the task
                    },
                    onTaskCheckboxClick = {
                        // TODO close or reopen task
                    },
                    onNavigateUp = navController::navigateUp,
                )
            }
            dialog(route = "addNewProject") {
                val viewModel: AddNewProjectViewModel = hiltViewModel()
                AddNewProjectScreen(
                    projectName = viewModel.projectName,
                    onProjectNameChange = viewModel::projectName::set,
                    addEnabled = viewModel.canAdd(),
                    onAdd = {
                        viewModel.addNewProject()
                        navController.navigateUp()
                    },
                )
            }
            composable(route = "project/{projectId}/task/{taskId}") { backStackEntry ->
                val viewModel: TaskViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("projectId"))
                val taskId = checkNotNull(backStackEntry.arguments?.getString("taskId"))
                viewModel.setup(projectId, taskId)

                TaskScreen(
                    task = viewModel.state.task,
                    onTaskCompletion = {
                        // TODO close or reopen task
                    },
                    onNavigateUp = navController::navigateUp,
                )
            }
            dialog(
                route = "addNewTask",
                dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                val viewModel: AddNewTaskViewModel = hiltViewModel()
                AddNewTaskScreen(
                    taskName = viewModel.taskName,
                    onTaskNameChange = viewModel::taskName::set,
                    taskContent = viewModel.taskContent,
                    onTaskContentChange = viewModel::taskContent::set,
                    addEnabled = viewModel.canAdd(),
                    onAdd = {
                        viewModel.addNewTask()
                        navController.navigateUp()
                    },
                )
            }
        }
    }
}
