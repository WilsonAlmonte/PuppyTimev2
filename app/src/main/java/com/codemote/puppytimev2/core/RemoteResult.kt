package com.codemote.puppytimev2.core

sealed interface RemoteResult<T>
data class Success<T>(val data: T) : RemoteResult<T>
data class Error<T>(val message: String) : RemoteResult<T>