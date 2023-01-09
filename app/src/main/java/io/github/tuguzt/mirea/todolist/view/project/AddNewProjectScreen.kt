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
import io.github.tuguzt.mirea.todolist.viewmodel.project.AddNewProjectScreenState
import io.github.tuguzt.mirea.todolist.viewmodel.project.NewProjectState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewProjectScreen(
    state: AddNewProjectScreenState,
    onProjectNameChange: (String) -> Unit,
    canAdd: Boolean = false,
    onAdd: () -> Unit = {},
) {
    if (state.newProjectState !is NewProjectState.ToBeCreated) {
        return
    }

    Surface(shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.enter_new_project_name),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.newProjectState.create.name,
                onValueChange = onProjectNameChange,
                enabled = !state.refreshing,
                label = { Text(text = stringResource(R.string.project_name)) },
                maxLines = 1,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.End),
                enabled = canAdd && !state.refreshing,
            ) {
                Text(text = stringResource(R.string.add_new_project))
            }

            if (state.refreshing) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.creating_new_project),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
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
private fun AddNewProjectScreen() {
    ToDoListTheme {
        SetupMaterial3RichText {
            val state = AddNewProjectScreenState()
            AddNewProjectScreen(
                state = state,
                onProjectNameChange = {},
            )
        }
    }
}

@Preview
@Composable
private fun AddNewProjectScreenRefreshing() {
    ToDoListTheme {
        SetupMaterial3RichText {
            val state = AddNewProjectScreenState(refreshing = true)
            AddNewProjectScreen(
                state = state,
                onProjectNameChange = {},
            )
        }
    }
}
