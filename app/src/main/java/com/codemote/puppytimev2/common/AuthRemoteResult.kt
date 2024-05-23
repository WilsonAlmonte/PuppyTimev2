package com.codemote.puppytimev2.common

sealed interface AuthRemoteResult : RemoteResult<Boolean>
data object SuccessAuthRemote : AuthRemoteResult
data class AuthenticationError(val message: String) : AuthRemoteResult
data class UnknownError(val message: String) : AuthRemoteResult
