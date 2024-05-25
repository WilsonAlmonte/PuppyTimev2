package com.codemote.puppytimev2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemote.puppytimev2.core.AuthState
import com.codemote.puppytimev2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authRepository: AuthRepository) : ViewModel() {
    val userSignedState = authRepository.getSignedState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AuthState.LOADING)
}