package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.*
import retrofit2.http.*

internal interface ProjectApi {
    @GET("projects")
    suspend fun all(): ApiResponse<List<ApiProject>>

    @GET("projects/{id}")
    suspend fun find(@Path("id") id: String): ApiResponse<ApiProject>

    @POST("projects")
    suspend fun create(@Body create: ApiCreateProject): ApiResponse<ApiProject>

    @POST("projects/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body update: ApiUpdateProject,
    ): ApiResponse<ApiProject>

    @DELETE("projects/{id}")
    suspend fun delete(@Path("id") id: String): ApiCompletableResponse
}
