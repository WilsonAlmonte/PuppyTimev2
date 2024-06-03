package com.codemote.puppytimev2.presentation.account

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.core.AuthenticationError
import com.codemote.puppytimev2.core.SuccessAuthRemote
import com.codemote.puppytimev2.core.UnknownError
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    fun processEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.AuthenticateUser -> authenticateUser()
            AccountEvent.OnCreateUser -> createUser()
            is AccountEvent.OnEmailInputChange -> onInputChange(
                event.value,
                AccountEvent.AccountInputType.EMAIL
            )

            is AccountEvent.OnNameInputChange -> onInputChange(
                event.value,
                AccountEvent.AccountInputType.NAME
            )

            is AccountEvent.OnPasswordInputChange -> onInputChange(
                event.value,
                AccountEvent.AccountInputType.PASSWORD
            )

            is AccountEvent.ShowSignUpBottomSheet -> toggleAccountBottomSheet(AccountBottomSheetType.SIGN_UP)
            is AccountEvent.ShowLoginBottomSheet -> toggleAccountBottomSheet(AccountBottomSheetType.LOGIN)
            is AccountEvent.HideAccountBottomSheet -> toggleAccountBottomSheet()
            AccountEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }

        }
    }

    private fun toggleAccountBottomSheet(sheetType: AccountBottomSheetType = AccountBottomSheetType.NONE) {
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
            )
        }
    }

    private fun onInputChange(value: String, accountInputType: AccountEvent.AccountInputType) {
        when (accountInputType) {
            AccountEvent.AccountInputType.EMAIL -> {
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

            AccountEvent.AccountInputType.PASSWORD -> {
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

            AccountEvent.AccountInputType.NAME -> userName = value
        }
    }

    private fun authenticateUser() {
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            _uiState.update { it.copy(effect = AccountEffect.ShowToast(appContext.getString(R.string.all_inputs_must_be_filled))) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = authRepository.authenticateUser(userEmail, userPassword)) {
                SuccessAuthRemote -> {
                    _uiState.update { it.copy(effect = AccountEffect.ShowToast("Welcome Back")) }
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
                            effect = AccountEffect.ShowToast(
                                result.message
                            )
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun createUser() {
        if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty()) {
            _uiState.update { it.copy(effect = AccountEffect.ShowToast(appContext.getString(R.string.all_inputs_must_be_filled))) }
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
                    _uiState.update { it.copy(effect = AccountEffect.ShowToast("Welcome to PUPPY TIME")) }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            effect = AccountEffect.ShowToast(appContext.getString(R.string.generic_error))
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    data class AccountUiState(
        val showAccountBottomSheet: Boolean = false,
        val accountBottomSheetType: AccountBottomSheetType = AccountBottomSheetType.NONE,
        val userEmailInputState: InputState = InputState(),
        val userPasswordInputState: InputState = InputState(),
        val isLoading: Boolean = false,
        val effect: AccountEffect? = null
    )

    sealed interface AccountEffect {
        data class ShowToast(val message: String) : AccountEffect
    }

    sealed class AccountEvent {
        data class OnEmailInputChange(val value: String) : AccountEvent()
        data class OnPasswordInputChange(val value: String) : AccountEvent()
        data class OnNameInputChange(val value: String) : AccountEvent()
        data object ShowSignUpBottomSheet : AccountEvent()
        data object ShowLoginBottomSheet : AccountEvent()
        data object HideAccountBottomSheet : AccountEvent()
        data object AuthenticateUser : AccountEvent()
        data object OnCreateUser : AccountEvent()
        data object ConsumeEffect : AccountEvent()
        enum class AccountInputType {
            EMAIL, PASSWORD, NAME
        }

    }

    data class InputState(val inputHasErrors: Boolean = false, val inputErrorMessage: String = "")
}
