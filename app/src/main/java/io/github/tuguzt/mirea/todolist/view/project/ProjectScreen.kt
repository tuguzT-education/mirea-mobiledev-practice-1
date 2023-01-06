package io.github.tuguzt.mirea.todolist.view.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.view.task.TaskCard
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import io.github.tuguzt.mirea.todolist.viewmodel.project.ProjectScreenState
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProjectScreen(
    state: ProjectScreenState,
    onAddNewTask: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onTaskDueClick: (Task) -> Unit = {},
    onTaskCheckboxClick: (Task) -> Unit = {},
    onDeleteProject: () -> Unit = {},
    onNavigateUp: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    pullRefreshState: PullRefreshState? = null,
) {
    val project = state.project ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = project.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    onNavigateUp?.let { onNavigateUp ->
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteProject) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete_project),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewTask) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_new_task),
                )
            }
        },
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(hostState = it)
            }
        },
    ) { padding ->
        Box(
            modifier = (pullRefreshState?.let { Modifier.pullRefresh(it) } ?: Modifier)
                .padding(padding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(project.tasks, key = { it.id.value }) { task ->
                    TaskCard(
                        task = task,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTaskClick(task) },
                        onDueClick = { onTaskDueClick(task) },
                        onCheckboxClick = { onTaskCheckboxClick(task) },
                    )
                }
            }
            pullRefreshState?.let {
                PullRefreshIndicator(
                    refreshing = state.isRefreshing,
                    state = it,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun ProjectScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            val project = Project(
                id = Id("42"),
                name = "My project",
                tasks = listOf(
                    Task(
                        id = Id("42"),
                        name = "Hello World",
                        completed = false,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now(),
                    ),
                    Task(
                        id = Id("43"),
                        name = "Some task",
                        completed = true,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now(),
                    ),
                ),
            )
            val state = ProjectScreenState(project = project, isRefreshing = false)
            ProjectScreen(state, onNavigateUp = {})
        }
    }
}
