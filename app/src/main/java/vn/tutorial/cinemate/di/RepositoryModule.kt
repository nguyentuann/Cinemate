package vn.tutorial.cinemate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorial.cinemate.data.repositoryImpl.AuthRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.FilmRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.NotificationRepositoryImpl
import vn.tutorial.cinemate.domain.repository.AuthRepository
import vn.tutorial.cinemate.domain.repository.FilmRepository
import vn.tutorial.cinemate.domain.repository.NotificationRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindFilmRepository(
        impl: FilmRepositoryImpl
    ): FilmRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}