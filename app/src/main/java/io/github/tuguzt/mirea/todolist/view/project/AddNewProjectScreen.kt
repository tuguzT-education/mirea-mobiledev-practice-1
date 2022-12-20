package io.github.tuguzt.mirea.todolist.view.project

import androidx.compose.foundation.layout.*
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
fun AddNewProjectScreen(
    projectName: String,
    onProjectNameChange: (String) -> Unit,
    addEnabled: Boolean = false,
    onAdd: () -> Unit = {},
) {
    Surface(shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.enter_new_project_name),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = projectName,
                onValueChange = onProjectNameChange,
                label = { Text(text = stringResource(R.string.project_name)) },
                maxLines = 1,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.End),
                enabled = addEnabled,
            ) {
                Text(text = stringResource(R.string.add_new_project))
            }
        }
    }
}

@Preview
@Composable
private fun AddNewProjectScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            AddNewProjectScreen(
                projectName = "",
                onProjectNameChange = {},
                onAdd = {},
            )
        }
    }
}
