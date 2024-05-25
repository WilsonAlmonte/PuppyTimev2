package com.codemote.puppytimev2.domain.repository

import com.codemote.puppytimev2.core.AuthRemoteResult
import com.codemote.puppytimev2.core.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createUser(email: String, name: String, password: String): AuthRemoteResult
    fun getCurrentUser(): Flow<FirebaseUser?>
    fun getSignedState(): Flow<AuthState>
    suspend fun authenticateUser(email: String, password: String): AuthRemoteResult
    fun signOut(): AuthRemoteResult
}