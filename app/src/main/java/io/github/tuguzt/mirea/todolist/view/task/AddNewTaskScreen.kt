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
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskScreen(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    taskContent: String,
    onTaskContentChange: (String) -> Unit,
    addEnabled: Boolean = false,
    onAdd: () -> Unit = {},
) {
    Surface(shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.enter_new_task_data),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = taskName,
                onValueChange = onTaskNameChange,
                label = { Text(text = stringResource(R.string.task_name)) },
                maxLines = 1,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = taskContent,
                onValueChange = onTaskContentChange,
                label = { Text(text = stringResource(R.string.task_content)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.End),
                enabled = addEnabled,
            ) {
                Text(text = stringResource(R.string.add_new_task))
            }
        }
    }
}

@Preview
@Composable
private fun AddNewTaskScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            AddNewTaskScreen(
                taskName = "Hello World",
                onTaskNameChange = {},
                taskContent = """
                    # Title
                    
                    Some supporting text
                    
                    ## Subtitle
                    
                    ```rust
                    fn main() {
                        println!("Hello World")
                    }
                    ```
                """.trimIndent(),
                onTaskContentChange = {},
            )
        }
    }
}
