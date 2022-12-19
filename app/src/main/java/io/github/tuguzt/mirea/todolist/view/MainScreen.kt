package io.github.tuguzt.mirea.todolist.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import io.github.tuguzt.mirea.todolist.R
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.view.project.ProjectCard
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    todoProjects: List<Project>,
    completedProjects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onAddNewProjectClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewProjectClick) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_new_project),
                )
            }
        },
    ) { padding ->
        ProjectList(
            todoProjects = todoProjects,
            completedProjects = completedProjects,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            onProjectClick = onProjectClick,
        )
    }
}

@Composable
private fun ProjectList(
    todoProjects: List<Project>,
    completedProjects: List<Project>,
    modifier: Modifier = Modifier,
    onProjectClick: (Project) -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (todoProjects.isNotEmpty()) {
            projectGroup(
                projects = todoProjects,
                header = context.getString(R.string.todo_projects),
                onProjectClick = onProjectClick,
            )
            item {}
        }
        if (completedProjects.isNotEmpty()) {
            projectGroup(
                projects = completedProjects,
                header = context.getString(R.string.completed_projects),
                onProjectClick = onProjectClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.projectGroup(
    projects: List<Project>,
    header: String,
    onProjectClick: (Project) -> Unit,
) {
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
            onClick = { onProjectClick(project) },
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
                        onProjectClick = {},
                    )
                }
            }
        }
    }
}
