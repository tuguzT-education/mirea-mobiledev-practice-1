package io.github.tuguzt.mirea.todolist.view.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconDestination : Destination {
    val icon: ImageVector
}
