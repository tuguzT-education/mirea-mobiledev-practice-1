package io.github.tuguzt.mirea.todolist.view.task

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RemoveDone
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import io.github.tuguzt.mirea.todolist.viewmodel.task.TaskScreenState
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    state: TaskScreenState,
    onTaskCompletion: () -> Unit = {},
    onDeleteTask: () -> Unit = {},
    onNavigateUp: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    pullRefreshState: PullRefreshState? = null,
) {
    val task = state.task ?: return

    val context = LocalContext.current
    val dateFormat = DateFormat.getLongDateFormat(context)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                MediumTopAppBar(
                    title = {
                        Text(
                            text = task.name,
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
                        IconButton(onClick = onTaskCompletion) {
                            when (task.completed) {
                                true -> Icon(
                                    imageVector = Icons.Rounded.RemoveDone,
                                    contentDescription = stringResource(R.string.mark_not_completed),
                                )
                                false -> Icon(
                                    imageVector = Icons.Rounded.DoneAll,
                                    contentDescription = stringResource(R.string.mark_completed),
                                )
                            }
                        }
                        IconButton(onClick = onDeleteTask) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = stringResource(R.string.delete_task),
                            )
                        }
                    },
                )

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                ) {
                    Text(
                        text = if (task.completed) {
                            stringResource(R.string.completed)
                        } else {
                            stringResource(R.string.not_completed)
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                    task.due?.let { due ->
                        Spacer(modifier = Modifier.height(8.dp))
                        val millis = due.datetime.toEpochMilliseconds()
                        Text(
                            text = "${due.string} (${dateFormat.format(millis)})",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val millis = task.createdAt.toEpochMilliseconds()
                    Text(
                        text = "Created at ${dateFormat.format(millis)}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
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
            RichText(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Markdown(content = task.content)
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
private fun TaskScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            val task = Task(
                id = Id("42"),
                name = "Hello World",
                content = """
                    # Title
                    
                    Some text
                    
                    ## Subtitle
                    
                    ```rust
                    fn main() {
                        println!("Hello World")
                    }
                    ```
                """.trimIndent(),
                completed = false,
                due = TaskDue(
                    string = "Tomorrow",
                    datetime = Clock.System.now() + 1.days,
                ),
                createdAt = Clock.System.now(),
            )
            val state = TaskScreenState(task = task, isRefreshing = false)
            TaskScreen(state, onNavigateUp = {})
        }
    }
}
