package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.*
import retrofit2.http.*

internal interface TaskApi {
    @GET("tasks")
    suspend fun all(@Query("project_id") projectId: String): ApiResponse<List<ApiTask>>

    @GET("tasks/{id}")
    suspend fun find(@Path("id") id: String): ApiResponse<ApiTask>

    @POST("tasks")
    suspend fun create(@Body create: ApiCreateTask): ApiResponse<ApiTask>

    @POST("tasks/{id}")
    suspend fun update(@Path("id") id: String, @Body update: ApiUpdateTask): ApiResponse<ApiTask>

    @POST("tasks/{id}/close")
    suspend fun close(@Path("id") id: String): ApiCompletableResponse

    @POST("tasks/{id}/reopen")
    suspend fun reopen(@Path("id") id: String): ApiCompletableResponse

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): ApiCompletableResponse
}
