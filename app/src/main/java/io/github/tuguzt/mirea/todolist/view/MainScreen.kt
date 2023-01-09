package io.github.tuguzt.mirea.todolist.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.view.project.ProjectCard
import io.github.tuguzt.mirea.todolist.view.theme.ToDoListTheme
import io.github.tuguzt.mirea.todolist.viewmodel.MainScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onProjectClick: (Project) -> Unit,
    onAddNewProjectClick: () -> Unit,
    snackbarHostState: SnackbarHostState? = null,
    pullRefreshState: PullRefreshState? = null,
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
            ProjectList(
                state = state,
                modifier = Modifier.fillMaxSize(),
                onProjectClick = onProjectClick,
            )
            pullRefreshState?.let {
                PullRefreshIndicator(
                    refreshing = state.refreshing,
                    state = it,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}

@Composable
private fun ProjectList(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onProjectClick: (Project) -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (state.projects == null) {
            ProjectGroup()
            return@LazyColumn
        }

        val todoProjects = requireNotNull(state.todoProjects)
        if (todoProjects.isNotEmpty()) {
            ProjectGroup(
                projects = todoProjects,
                header = context.getString(R.string.todo_projects),
                onProjectClick = onProjectClick,
            )
            item {}
        }

        val completedProjects = requireNotNull(state.completedProjects)
        if (completedProjects.isNotEmpty()) {
            ProjectGroup(
                projects = completedProjects,
                header = context.getString(R.string.completed_projects),
                onProjectClick = onProjectClick,
            )
        }
    }
}

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.ProjectGroup(
    projects: List<Project>,
    header: String,
    onProjectClick: (Project) -> Unit,
) {
    stickyHeader {
        Surface(tonalElevation = 2.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(text = header)
            }
        }
    }
    items(projects, key = { it.id.value }) { project ->
        ProjectCard(
            project = project,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onProjectClick(project) },
        )
    }
}

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.ProjectGroup() {
    stickyHeader {
        Surface(tonalElevation = 2.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "Placeholder group",
                    modifier = Modifier
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.fade(),
                        ),
                )
            }
        }
    }
    items(3) {
        ProjectCard(
            project = null,
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
    val projects = listOf(
        Project(
            id = Id("42"),
            name = "Hello World",
            tasks = listOf(),
        ),
    )

    ToDoListTheme {
        SetupMaterial3RichText {
            Scaffold { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ProjectGroup(
                        projects = projects,
                        header = "Hello World",
                        onProjectClick = {},
                    )
                }
            }
        }
    }
}
