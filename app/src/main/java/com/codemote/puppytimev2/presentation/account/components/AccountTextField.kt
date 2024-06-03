package com.codemote.puppytimev2.presentation.account.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.presentation.account.AccountViewModel
import com.codemote.puppytimev2.ui.atoms.PuppyTextField

@Composable
fun EmailTextField(
    userEmailInputState: AccountViewModel.InputState,
    userEmail: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = stringResource(R.string.email),
        value = userEmail,
        enabled = enabled,
        onValueChange = { onValueChange(it) },
        isError = userEmailInputState.inputHasErrors,
        errorMessage = userEmailInputState.inputErrorMessage,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon"
            )
        }
    )
}

@Composable
fun NameTextField(
    userName: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = stringResource(R.string.name),
        value = userName,
        enabled = enabled,
        onValueChange = { onValueChange(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Name Icon"
            )
        }
    )
}

@Composable
fun PasswordTextField(
    userPasswordInputState: AccountViewModel.InputState,
    userPassword: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = stringResource(R.string.password),
        value = userPassword,
        onValueChange = { onValueChange(it) },
        isError = userPasswordInputState.inputHasErrors,
        errorMessage = userPasswordInputState.inputErrorMessage,
        visualTransformation = PasswordVisualTransformation(),
        enabled = enabled,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon"
            )
        },
        modifier = Modifier.padding(bottom = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )
    )
}