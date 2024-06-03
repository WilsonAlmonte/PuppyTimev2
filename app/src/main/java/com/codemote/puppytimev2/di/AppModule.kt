package com.codemote.puppytimev2.di

import android.app.Application
import com.codemote.puppytimev2.data.repository.AuthRepositoryImpl
import com.codemote.puppytimev2.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(appContext: Application): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = Firebase.auth, appContext = appContext)
    }
}