package vn.tutorial.cinemate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorial.cinemate.data.repositoryImpl.AuthRepositoryImpl
import vn.tutorial.cinemate.domain.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}