package io.github.tuguzt.mirea.todolist.view.task

import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task?,
    modifier: Modifier = Modifier,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
    onDueClick: () -> Unit = {},
    onCheckboxClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val dateFormat = DateFormat.getMediumDateFormat(context)

    Card(
        modifier = modifier,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (task == null) {
                Checkbox(
                    checked = false,
                    enabled = false,
                    onCheckedChange = { onCheckboxClick() },
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Placeholder text that no one will see",
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.fade(),
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(
                        onClick = onDueClick,
                        enabled = false,
                        label = {
                            val millis = Clock.System.now().toEpochMilliseconds()
                            Text(
                                text = dateFormat.format(millis),
                                modifier = Modifier.placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.fade(),
                                ),
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Event,
                                contentDescription = stringResource(R.string.task_due),
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                            )
                        },
                    )
                }
                return@Row
            }

            Checkbox(
                checked = task.completed,
                onCheckedChange = { onCheckboxClick() },
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = task.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                )
                task.due?.datetime?.let { datetime ->
                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(
                        onClick = onDueClick,
                        label = {
                            val millis = datetime.toEpochMilliseconds()
                            Text(text = dateFormat.format(millis))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Event,
                                contentDescription = stringResource(R.string.task_due),
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCard() {
    val task = Task(
        id = Id("42"),
        name = "Hello `World`",
        completed = true,
        content = "Some task content",
        due = TaskDue(
            string = "",
            datetime = Clock.System.now(),
        ),
        createdAt = Clock.System.now(),
    )

    ToDoListTheme {
        SetupMaterial3RichText {
            TaskCard(
                task = task,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun TaskCardPlaceholder() {
    ToDoListTheme {
        SetupMaterial3RichText {
            TaskCard(
                task = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
