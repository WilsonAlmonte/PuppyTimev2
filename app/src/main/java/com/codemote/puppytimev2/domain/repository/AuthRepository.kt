package com.codemote.puppytimev2.domain.repository

import com.codemote.puppytimev2.common.AuthRemoteResult
import com.codemote.puppytimev2.common.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun createUser(email: String, name: String, password: String): AuthRemoteResult
    fun getCurrentUser(viewModelScope: CoroutineScope): StateFlow<FirebaseUser?>
    fun getSignedState(viewModelScope: CoroutineScope): StateFlow<AuthState>
    suspend fun authenticateUser(email: String, password: String): AuthRemoteResult
    fun signOut(): AuthRemoteResult
}