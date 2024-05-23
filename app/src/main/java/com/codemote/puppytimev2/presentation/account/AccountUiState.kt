package com.codemote.puppytimev2.presentation.account

data class AccountUiState(
    val showAccountBottomSheet: Boolean = false,
    val accountBottomSheetType: AccountBottomSheetType = AccountBottomSheetType.NONE,
    val userEmailInputState: InputState = InputState(),
    val userPasswordInputState: InputState = InputState(),
    val accountScreenError: String? = null,
    val isLoading: Boolean = false
)

enum class AccountBottomSheetType {
    NONE,
    LOGIN,
    SIGN_UP
}

data class InputState(val inputHasErrors: Boolean = false, val inputErrorMessage: String = "")