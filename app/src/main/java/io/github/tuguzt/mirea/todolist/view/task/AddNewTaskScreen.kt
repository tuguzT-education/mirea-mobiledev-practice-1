package io.github.tuguzt.mirea.todolist.view.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.CreateTask
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import io.github.tuguzt.mirea.todolist.viewmodel.task.AddNewTaskScreenState
import io.github.tuguzt.mirea.todolist.viewmodel.task.NewTaskState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskScreen(
    state: AddNewTaskScreenState,
    onTaskNameChange: (String) -> Unit,
    onTaskContentChange: (String) -> Unit,
    canAdd: Boolean = false,
    onAdd: () -> Unit = {},
) {
    if (state.newTaskState is NewTaskState.Created) {
        return
    }

    Surface(shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.enter_new_task_data),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = (state.newTaskState as? NewTaskState.ToBeCreated)?.create?.name ?: "",
                onValueChange = onTaskNameChange,
                enabled = !state.refreshing && state.newTaskState is NewTaskState.ToBeCreated,
                label = { Text(text = stringResource(R.string.task_name)) },
                maxLines = 1,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = (state.newTaskState as? NewTaskState.ToBeCreated)?.create?.content ?: "",
                onValueChange = onTaskContentChange,
                enabled = !state.refreshing && state.newTaskState is NewTaskState.ToBeCreated,
                label = { Text(text = stringResource(R.string.task_content)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.End),
                enabled = canAdd && !state.refreshing,
            ) {
                Text(text = stringResource(R.string.add_new_task))
            }

            if (state.refreshing) {
                if (state.newTaskState is NewTaskState.ToBeCreated) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.creating_new_task),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddNewTaskScreen() {
    val create = CreateTask(
        project = Id("42"),
        name = "Hello World",
        content = """
            # Title
            
            Some supporting text
            
            ## Subtitle
            
            ```rust
            fn main() {
                println!("Hello World")
            }
            ```
        """.trimIndent(),
    )

    ToDoListTheme {
        SetupMaterial3RichText {
            val state = AddNewTaskScreenState(newTaskState = NewTaskState.ToBeCreated(create))
            AddNewTaskScreen(
                state = state,
                onTaskNameChange = {},
                onTaskContentChange = {},
            )
        }
    }
}
