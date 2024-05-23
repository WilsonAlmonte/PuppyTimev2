package com.codemote.puppytimev2.data.repository

import com.codemote.puppytimev2.common.AuthRemoteResult
import com.codemote.puppytimev2.common.AuthState
import com.codemote.puppytimev2.common.AuthenticationError
import com.codemote.puppytimev2.common.SuccessAuthRemote
import com.codemote.puppytimev2.common.UnknownError
import com.codemote.puppytimev2.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    override suspend fun authenticateUser(
        email: String,
        password: String
    ): AuthRemoteResult = withContext(ioDispatcher) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return@withContext SuccessAuthRemote
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return@withContext AuthenticationError(message = "The combination of email and password is incorrect")
        } catch (e: Exception) {
            return@withContext UnknownError(message = "An unexpected error has occurred")
        }
    }

    override fun signOut(): AuthRemoteResult {
        try {
            firebaseAuth.signOut()
            return SuccessAuthRemote
        } catch (e: Exception) {
            return UnknownError(message = "An error occurred")
        }
    }

    override fun getCurrentUser(viewModelScope: CoroutineScope): StateFlow<FirebaseUser?> =
        callbackFlow {
            val currentUserListener = AuthStateListener {
                trySend(it.currentUser)
            }
            firebaseAuth.addAuthStateListener(currentUserListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(currentUserListener)
            }
        }.flowOn(ioDispatcher)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser)

    override fun getSignedState(viewModelScope: CoroutineScope): StateFlow<AuthState> =
        callbackFlow {
            val authStateListener = AuthStateListener {
                trySend(AuthState.LOADING)
                val state =
                    if (it.currentUser == null) AuthState.SIGNED_OUT else AuthState.SIGNED_IN
                trySend(state)
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }.flowOn(ioDispatcher)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AuthState.LOADING)

    override suspend fun createUser(
        email: String,
        name: String,
        password: String
    ): AuthRemoteResult {
        try {
            println(password)
            val newUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (newUser.user != null) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                val user = firebaseAuth.currentUser
                user!!.updateProfile(profileUpdates).await()
                return SuccessAuthRemote
            }
            throw Exception("User is null")
        } catch (e: Exception) {
            return UnknownError(message = e.message ?: "")
        }
    }
}