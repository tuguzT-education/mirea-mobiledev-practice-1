package io.github.tuguzt.mirea.todolist.view.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project?,
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
            if (project == null) {
                Text(
                    text = "Some placeholder text that will not be visible either",
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.fade(),
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "69 from 420",
                    modifier = Modifier.placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.fade(),
                    ),
                )
                return@Column
            }

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
                        val completedTaskCount = stringResource(
                            id = R.string.project_completed_task_count,
                            formatArgs = arrayOf(completedTasks, totalTasks),
                        )
                        append(completedTaskCount)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProjectCard() {
    val project = Project(
        id = Id("42"),
        name = "New project",
        tasks = listOf(
            Task(
                id = Id("43"),
                name = "Hello World",
                completed = false,
                content = "",
                due = null,
                createdAt = Clock.System.now(),
            ),
            Task(
                id = Id("44"),
                name = "Some task",
                completed = true,
                content = "",
                due = null,
                createdAt = Clock.System.now(),
            ),
        ),
    )

    ToDoListTheme {
        SetupMaterial3RichText {
            ProjectCard(
                project = project,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun ProjectCardPlaceholder() {
    ToDoListTheme {
        SetupMaterial3RichText {
            ProjectCard(
                project = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
