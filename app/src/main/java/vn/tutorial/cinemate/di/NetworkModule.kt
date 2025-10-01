package vn.tutorial.cinemate.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.tutorial.cinemate.core.constant.ApiEndpoints
import vn.tutorial.cinemate.data.remote.services.AuthService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ApiEndpoints.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}

