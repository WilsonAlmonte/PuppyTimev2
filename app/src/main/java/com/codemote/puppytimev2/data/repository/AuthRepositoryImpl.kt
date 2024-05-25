package com.codemote.puppytimev2.data.repository

import android.app.Application
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.core.AuthRemoteResult
import com.codemote.puppytimev2.core.AuthState
import com.codemote.puppytimev2.core.AuthenticationError
import com.codemote.puppytimev2.core.SuccessAuthRemote
import com.codemote.puppytimev2.core.UnknownError
import com.codemote.puppytimev2.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val appContext: Application,
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
            return@withContext AuthenticationError(message = appContext.getString(R.string.authentication_error))
        } catch (e: Exception) {
            return@withContext UnknownError(message = appContext.getString(R.string.generic_error))
        }
    }

    override fun signOut(): AuthRemoteResult {
        try {
            firebaseAuth.signOut()
            return SuccessAuthRemote
        } catch (e: Exception) {
            return UnknownError(message = appContext.getString(R.string.generic_error))
        }
    }

    override fun getCurrentUser(): Flow<FirebaseUser?> =
        callbackFlow {
            val currentUserListener = AuthStateListener {
                trySend(it.currentUser)
            }
            firebaseAuth.addAuthStateListener(currentUserListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(currentUserListener)
            }
        }.flowOn(ioDispatcher)

    override fun getSignedState(): Flow<AuthState> =
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


    override suspend fun createUser(
        email: String,
        name: String,
        password: String
    ): AuthRemoteResult = withContext(ioDispatcher) {
        try {
            val newUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (newUser.user != null) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                val user = firebaseAuth.currentUser
                user!!.updateProfile(profileUpdates).await()
                return@withContext SuccessAuthRemote
            }
            throw Exception(appContext.getString(R.string.generic_error))
        } catch (e: Exception) {
            return@withContext UnknownError(message = e.message ?: "")
        }
    }
}