package com.codemote.puppytimev2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemote.puppytimev2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authRepository: AuthRepository) : ViewModel() {
    val userSignedState = authRepository.getSignedState(viewModelScope)
}