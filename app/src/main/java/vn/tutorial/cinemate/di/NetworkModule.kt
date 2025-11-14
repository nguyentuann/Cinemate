package vn.tutorial.cinemate.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint
import vn.tutorial.cinemate.data.remote.interceptor.AuthInterceptor
import vn.tutorial.cinemate.data.remote.services.AuthService
import vn.tutorial.cinemate.data.remote.services.FavoriteService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.data.remote.services.ProfileService
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BaseEndpoint.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()

//    @Singleton
//    @AuthRetrofit
//    @Provides
//    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
//        .baseUrl(BaseEndpoint.AUTH_BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(
//            GsonConverterFactory.create()
//        ).build()
//
//    @Singleton
//    @MovieRetrofit
//    @Provides
//    fun provideMovieRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
//        .baseUrl(BaseEndpoint.AVT_URL)
//        .client(okHttpClient)
//        .addConverterFactory(
//            GsonConverterFactory.create()
//        ).build()
//
//    @Singleton
//    @FavoriteRetrofit
//    @Provides
//    fun provideFavoriteRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
//        .baseUrl(BaseEndpoint.FAVORITE_BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(
//            GsonConverterFactory.create()
//        ).build()

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieService( retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Singleton
    @Provides
    fun provideFavoriteService(retrofit: Retrofit): FavoriteService {
        return retrofit.create(FavoriteService::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FavoriteRetrofit


