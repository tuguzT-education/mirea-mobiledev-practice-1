package io.github.tuguzt.mirea.todolist.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.view.project.ProjectCard
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = viewModel()) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_new_project),
                )
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val todoProjects = viewModel.todoProjects()
            if (todoProjects.isNotEmpty()) {
                projectGroup(
                    projects = todoProjects,
                    header = context.getString(R.string.todo_projects),
                )
                item {}
            }
            val completedProjects = viewModel.completedProjects()
            if (completedProjects.isNotEmpty()) {
                projectGroup(
                    projects = completedProjects,
                    header = context.getString(R.string.completed_projects),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.projectGroup(projects: List<Project>, header: String) {
    stickyHeader {
        Surface(tonalElevation = 2.dp) {
            Text(
                text = header,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .padding(start = 16.dp),
            )
        }
    }
    items(projects, key = Project::id) { project ->
        ProjectCard(
            project = project,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProjectGroup() {
    ToDoListTheme {
        SetupMaterial3RichText {
            Scaffold { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val projects = listOf(
                        Project(
                            id = "42",
                            name = "Hello World",
                            tasks = listOf(),
                        ),
                    )
                    projectGroup(
                        projects = projects,
                        header = "Hello World",
                    )
                }
            }
        }
    }
}
