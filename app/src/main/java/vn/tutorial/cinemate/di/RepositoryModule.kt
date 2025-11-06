package vn.tutorial.cinemate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorial.cinemate.data.repositoryImpl.AuthRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.CategoryRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.FavoriteRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.MovieRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.NotificationRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.ProfileRepositoryImpl
import vn.tutorial.cinemate.data.repositoryImpl.ReviewRepositoryImpl
import vn.tutorial.cinemate.domain.repository.AuthRepository
import vn.tutorial.cinemate.domain.repository.CategoryRepository
import vn.tutorial.cinemate.domain.repository.FavoriteRepository
import vn.tutorial.cinemate.domain.repository.MovieRepository
import vn.tutorial.cinemate.domain.repository.NotificationRepository
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import vn.tutorial.cinemate.domain.repository.ReviewRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindFilmRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    abstract fun bindReviewRepository(
        impl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}