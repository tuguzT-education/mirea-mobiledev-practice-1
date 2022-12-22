package io.github.tuguzt.mirea.todolist.data.datasource.local

import android.content.Context
import io.github.tuguzt.mirea.todolist.data.BuildConfig
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.MyObjectBox
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import io.objectbox.kotlin.boxFor

public class DatabaseClient(context: Context) {
    internal val boxStore: BoxStore = MyObjectBox.builder()
        .androidContext(context)
        .build()
        .apply {
            if (BuildConfig.DEBUG) {
                val admin = Admin(this)
                admin.start(context)
            }
        }

    internal val projectBox: Box<ProjectEntity> = boxStore.boxFor()

    internal val taskBox: Box<TaskEntity> = boxStore.boxFor()
}
