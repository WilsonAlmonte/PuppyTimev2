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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.codemote.puppytimev2.presentation.account.AccountInputType
import com.codemote.puppytimev2.presentation.account.InputState
import com.codemote.puppytimev2.ui.atoms.PuppyTextField

@Composable
fun EmailTextField(
    userEmailInputState: InputState,
    userEmail: String,
    onValueChange: (String, AccountInputType) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = "Email",
        value = userEmail,
        enabled = enabled,
        onValueChange = { onValueChange(it, AccountInputType.EMAIL) },
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
    onValueChange: (String, AccountInputType) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = "Name",
        value = userName,
        enabled = enabled,
        onValueChange = { onValueChange(it, AccountInputType.NAME) },
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
    userPasswordInputState: InputState,
    userPassword: String,
    onValueChange: (String, AccountInputType) -> Unit,
    enabled: Boolean,
) {
    PuppyTextField(
        labelText = "Password",
        value = userPassword,
        onValueChange = { onValueChange(it, AccountInputType.PASSWORD) },
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