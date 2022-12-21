package io.github.tuguzt.mirea.todolist.data.datasource.local.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany

@Entity
internal data class ProjectEntity(
    @Id var id: Long = 0,
    @Unique var uid: String? = null,
    var name: String? = null,
) {
    lateinit var tasks: ToMany<TaskEntity>
}
