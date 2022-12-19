package io.github.tuguzt.mirea.todolist.view.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.name,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
            )
            if (project.tasks.isNotEmpty()) {
                val totalTasks = project.tasks.size
                val completedTasks = project.tasks.count(Task::completed)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        if (totalTasks == completedTasks) append("✔ ")
                        append("$completedTasks from $totalTasks")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProjectCardPreview() {
    ToDoListTheme {
        ProjectCard(
            project = Project(
                id = "42",
                name = "New project",
                tasks = listOf(
                    Task(
                        id = "42",
                        name = "Hello World",
                        completed = false,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now()
                    ),
                    Task(
                        id = "43",
                        name = "Some task",
                        completed = true,
                        content = "",
                        due = null,
                        createdAt = Clock.System.now()
                    ),
                ),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
