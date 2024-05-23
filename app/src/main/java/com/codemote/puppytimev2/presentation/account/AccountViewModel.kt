package com.codemote.puppytimev2.presentation.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemote.puppytimev2.common.AuthenticationError
import com.codemote.puppytimev2.common.SuccessAuthRemote
import com.codemote.puppytimev2.common.UnknownError
import com.codemote.puppytimev2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    fun toggleAccountBottomSheet(sheetType: AccountBottomSheetType = AccountBottomSheetType.NONE) {
        if (uiState.value.showAccountBottomSheet) {
            resetInputs()
        }
        _uiState.update {
            it.copy(
                accountBottomSheetType = sheetType,
                showAccountBottomSheet = !it.showAccountBottomSheet
            )
        }
    }

    private fun resetInputs() {
        userEmail = ""
        userName = ""
        userPassword = ""
        _uiState.update {
            it.copy(
                userEmailInputState = InputState(),
                userPasswordInputState = InputState()
            )
        }
    }

    fun onInputChange(value: String, accountInputType: AccountInputType) {
        if (_uiState.value.unKnownError !== null) {
            _uiState.update { it.copy(unKnownError = null) }
        }
        when (accountInputType) {
            AccountInputType.EMAIL -> {
                userEmail = value
                _uiState.update {
                    it.copy(
                        userEmailInputState = if (value.isNotEmpty()) {
                            InputState(
                                inputHasErrors = !android.util.Patterns.EMAIL_ADDRESS.matcher(value)
                                    .matches(),
                                inputErrorMessage = "Email format is incorrect"
                            )
                        } else {
                            InputState()
                        }
                    )
                }
            }

            AccountInputType.PASSWORD -> {
                userPassword = value
                _uiState.update {
                    it.copy(
                        userPasswordInputState = if (value.isNotEmpty()) {
                            InputState(
                                inputHasErrors = value.length < 6,
                                inputErrorMessage = "Password must have at least 6 characters"
                            )
                        } else {
                            InputState()
                        }
                    )
                }
            }

            AccountInputType.NAME -> userName = value
        }
    }

    fun authenticateUser(onSuccessfulAuthentication: () -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = authRepository.authenticateUser(userEmail, userPassword)) {
                SuccessAuthRemote -> {
                    onSuccessfulAuthentication()
                }

                is AuthenticationError -> {
                    _uiState.update {
                        it.copy(
                            userPasswordInputState = InputState(
                                inputHasErrors = true,
                                inputErrorMessage = result.message
                            )
                        )
                    }
                }

                is UnknownError -> {
                    _uiState.update {
                        it.copy(
                            unKnownError = result.message
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun createUser(onSuccessfulUserCreation: () -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (authRepository.createUser(
                email = userEmail,
                password = userPassword,
                name = userName
            )) {
                SuccessAuthRemote -> {
                    onSuccessfulUserCreation()
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            unKnownError = "An unknown error has happened"
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

enum class AccountInputType {
    EMAIL, PASSWORD, NAME
}