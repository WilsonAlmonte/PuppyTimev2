package com.codemote.puppytimev2.presentation.home

import androidx.lifecycle.ViewModel
import com.codemote.puppytimev2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    fun signOut() {
        authRepository.signOut()
    }
}