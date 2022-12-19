package io.github.tuguzt.mirea.todolist.view.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.view.task.TaskCard
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    project: Project,
    onAddNewTask: () -> Unit = {},
    onNavigateUp: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(project.name, maxLines = 1) },
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(project.tasks, key = Task::id) { task ->
                TaskCard(task, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
private fun ProjectScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            val project = Project(
                id = "42",
                name = "My project",
                tasks = listOf(
                    Task(
                        id = "42",
                        name = "Hello World",
                        completed = false,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now(),
                    ),
                    Task(
                        id = "43",
                        name = "Some task",
                        completed = true,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now(),
                    ),
                ),
            )
            ProjectScreen(project, onNavigateUp = {})
        }
    }
}
