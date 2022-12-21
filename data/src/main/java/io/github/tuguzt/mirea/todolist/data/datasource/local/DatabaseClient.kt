package io.github.tuguzt.mirea.todolist.data.datasource.local

import android.content.Context
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.MyObjectBox
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

public class DatabaseClient(context: Context) {
    private val store: BoxStore = MyObjectBox.builder()
        .androidContext(context)
        .build()

    internal val projectBox: Box<ProjectEntity> = store.boxFor()

    internal val taskBox: Box<TaskEntity> = store.boxFor()
}
