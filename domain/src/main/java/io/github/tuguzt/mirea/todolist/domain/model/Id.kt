package io.github.tuguzt.mirea.todolist.domain.model

@JvmInline
@Suppress("unused")
public value class Id<Owner : Node>(public val value: String) {
    override fun toString(): String = value
}

public fun <Owner : Node, Other : Node> Id<Owner>.changeOwner(): Id<Other> = Id(value)
