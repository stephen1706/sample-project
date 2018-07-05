package com.stephen.shopback.api

import android.util.Log
import com.stephen.shopback.utils.RxErrorHandlingCallAdapterFactory
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface ApiService {
    @GET
    fun getUsers(@Url url: String): Single<Response<List<UserDataModel>>>

    @GET(ApiRoutes.USER_DETAIL_ROUTES)
    fun getUserDetail(@Path("id") id: String): Single<UserDataModel>


    companion object {
        private const val BASE_URL = "https://api.github.com/"
        fun create(): ApiService = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): ApiService {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
            builder.addInterceptor(logger)

            val client = builder.build()

            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
        }
    }
}