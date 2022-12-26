package io.github.tuguzt.mirea.todolist.data.datasource.remote

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

public class ApiClient {
    private companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val converterFactory = Json.asConverterFactory("application/json".toMediaType())

        const val apiToken = "SECRET"

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $apiToken")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.todoist.com/rest/v2/")
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()

    internal val projectApi: ProjectApi = retrofit.create()

    internal val taskApi: TaskApi = retrofit.create()
}
