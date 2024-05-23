package com.codemote.puppytimev2.presentation.account

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemote.puppytimev2.R
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
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appContext: Application
) :
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
                userPasswordInputState = InputState(),
                accountScreenError = null
            )
        }
    }

    fun onInputChange(value: String, accountInputType: AccountInputType) {
        if (_uiState.value.accountScreenError !== null) {
            _uiState.update { it.copy(accountScreenError = null) }
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
                                inputErrorMessage = appContext.getString(R.string.email_format_error)
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
                                inputErrorMessage = appContext.getString(R.string.password_error)
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
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            _uiState.update { it.copy(accountScreenError = appContext.getString(R.string.all_inputs_must_be_filled)) }
            return
        }
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
                            accountScreenError = result.message
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun createUser(onSuccessfulUserCreation: () -> Unit) {
        if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty()) {
            _uiState.update { it.copy(accountScreenError = appContext.getString(R.string.all_inputs_must_be_filled)) }
            return
        }
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
                            accountScreenError = appContext.getString(R.string.generic_error)
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