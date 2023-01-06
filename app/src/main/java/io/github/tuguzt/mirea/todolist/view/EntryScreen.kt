package io.github.tuguzt.mirea.todolist.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.view.project.AddNewProjectScreen
import io.github.tuguzt.mirea.todolist.view.project.ProjectScreen
import io.github.tuguzt.mirea.todolist.view.task.AddNewTaskScreen
import io.github.tuguzt.mirea.todolist.view.task.TaskScreen
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.project.AddNewProjectViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.project.ProjectViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.task.AddNewTaskViewModel
import io.github.tuguzt.mirea.todolist.viewmodel.task.TaskViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun EntryScreen(
    mainViewModel: MainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "main") {
            composable(route = "main") {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = mainViewModel.state.isRefreshing,
                    onRefresh = mainViewModel::refresh,
                )
                MainScreen(
                    state = mainViewModel.state,
                    onProjectClick = { project ->
                        navController.navigate(route = "project/${project.id}")
                    },
                    onAddNewProjectClick = {
                        navController.navigate(route = "addNewProject")
                    },
                    snackbarHostState = snackbarHostState,
                    pullRefreshState = pullRefreshState,
                )
            }
            composable(route = "project/{id}") { backStackEntry ->
                val viewModel: ProjectViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("id"))
                LaunchedEffect(projectId) {
                    val id = Id<Project>(projectId)
                    viewModel.setup(id)
                }

                if (!viewModel.state.loading) {
                    ProjectScreen(
                        project = checkNotNull(viewModel.state.project),
                        onAddNewTask = {
                            navController.navigate(route = "project/$projectId/addNewTask")
                        },
                        onTaskClick = { task ->
                            navController.navigate(route = "task/${task.id}")
                        },
                        onTaskDueClick = {
                            // TODO change due of the task
                        },
                        onTaskCheckboxClick = viewModel::changeTaskCompletion,
                        onDeleteProject = {
                            viewModel.deleteProject()
                            mainViewModel.refresh()
                            navController.navigateUp()
                        },
                        onNavigateUp = navController::navigateUp,
                        snackbarHostState = snackbarHostState,
                    )
                }

                viewModel.state.userMessages.firstOrNull()?.let { message ->
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(
                            message = message.kind.name,
                            actionLabel = context.getString(R.string.dismiss),
                        )
                        viewModel.userMessageShown(message.id)
                    }
                }
            }
            dialog(route = "addNewProject") {
                val viewModel: AddNewProjectViewModel = hiltViewModel()
                AddNewProjectScreen(
                    projectName = viewModel.projectName,
                    onProjectNameChange = viewModel::projectName::set,
                    addEnabled = viewModel.canAdd(),
                    onAdd = {
                        viewModel.addNewProject()
                        mainViewModel.refresh()
                        navController.navigateUp()
                    },
                )

                viewModel.state.userMessages.firstOrNull()?.let { message ->
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(
                            message = message.kind.name,
                            actionLabel = context.getString(R.string.dismiss),
                        )
                        viewModel.userMessageShown(message.id)
                    }
                }
            }
            composable(route = "task/{taskId}") { backStackEntry ->
                val viewModel: TaskViewModel = hiltViewModel()
                val taskId = checkNotNull(backStackEntry.arguments?.getString("taskId"))
                LaunchedEffect(taskId) {
                    val id = Id<Task>(taskId)
                    viewModel.setup(id)
                }

                if (!viewModel.state.loading) {
                    TaskScreen(
                        task = checkNotNull(viewModel.state.task),
                        onTaskCompletion = viewModel::changeTaskCompletion,
                        onDeleteTask = {
                            viewModel.deleteTask()
                            navController.navigateUp()
                        },
                        onNavigateUp = navController::navigateUp,
                        snackbarHostState = snackbarHostState,
                    )
                }

                viewModel.state.userMessages.firstOrNull()?.let { message ->
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(
                            message = message.kind.name,
                            actionLabel = context.getString(R.string.dismiss),
                        )
                        viewModel.userMessageShown(message.id)
                    }
                }
            }
            dialog(
                route = "project/{id}/addNewTask",
                dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
            ) { backStackEntry ->
                val viewModel: AddNewTaskViewModel = hiltViewModel()
                val projectId = checkNotNull(backStackEntry.arguments?.getString("id"))
                LaunchedEffect(projectId) {
                    val id = Id<Project>(projectId)
                    viewModel.setup(id)
                }

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

                viewModel.state.userMessages.firstOrNull()?.let { message ->
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(
                            message = message.kind.name,
                            actionLabel = context.getString(R.string.dismiss),
                        )
                        viewModel.userMessageShown(message.id)
                    }
                }
            }
        }
    }

    mainViewModel.state.userMessages.firstOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message.kind.name,
                actionLabel = context.getString(R.string.dismiss),
            )
            mainViewModel.userMessageShown(message.id)
        }
    }
}
