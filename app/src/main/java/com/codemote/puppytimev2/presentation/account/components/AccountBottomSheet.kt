package com.codemote.puppytimev2.presentation.account.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.presentation.account.AccountViewModel
import com.codemote.puppytimev2.ui.atoms.PuppyPrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    content: @Composable () -> Unit
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            onDismissRequest = onDismiss,
            dragHandle = {
                Icon(
                    painter = painterResource(id = R.drawable.paw),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 36.dp, bottom = 8.dp)
                        .size(24.dp)
                        .fillMaxHeight()
                        .clickable { }
                )
            },
            sheetState = sheetState
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .fillMaxSize(),
            ) {
                content()
            }
        }
    }
}

@Composable
fun LoginLayout(
    userEmail: String,
    userEmailInputState: AccountViewModel.InputState,
    userPasswordInputState: AccountViewModel.InputState,
    userPassword: String,
    onEvent: (AccountViewModel.AccountEvent) -> Unit,
    operationIsLoading: Boolean
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp, top = 16.dp),
        text = stringResource(R.string.login_layout_title),
        style = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        text = stringResource(R.string.login_layout_subtitle),
        style = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
        )
    )
    EmailTextField(
        enabled = !operationIsLoading,
        userEmailInputState = userEmailInputState,
        userEmail = userEmail,
        onValueChange = { onEvent(AccountViewModel.AccountEvent.OnEmailInputChange(it)) }
    )
    PasswordTextField(
        enabled = !operationIsLoading,
        userPasswordInputState = userPasswordInputState,
        userPassword = userPassword,
        onValueChange = { onEvent(AccountViewModel.AccountEvent.OnPasswordInputChange(it)) }
    )
    if (operationIsLoading) {
        CircularProgressIndicator(
            strokeWidth = 5.dp,
            trackColor = MaterialTheme.colorScheme.secondary,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(100.dp)
        )
    } else {
        PuppyPrimaryButton(
            buttonText = stringResource(id = R.string.sign_in_button_text),
            onClick = { onEvent(AccountViewModel.AccountEvent.AuthenticateUser) }
        )
    }
}

@Composable
fun SignUpLayout(
    userEmail: String,
    userPassword: String,
    userEmailInputState: AccountViewModel.InputState,
    userPasswordInputState: AccountViewModel.InputState,
    userName: String,
    onEvent: (AccountViewModel.AccountEvent) -> Unit,
    operationIsLoading: Boolean
) {

    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(R.string.signup_layout_title),
        style = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        text = stringResource(R.string.sign_up_layout_subtitle),
        style = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
        )
    )
    NameTextField(
        userName = userName,
        onValueChange = { onEvent(AccountViewModel.AccountEvent.OnNameInputChange(it)) },
        enabled = !operationIsLoading
    )
    EmailTextField(
        userEmailInputState = userEmailInputState,
        userEmail = userEmail,
        onValueChange = { onEvent(AccountViewModel.AccountEvent.OnEmailInputChange(it)) },
        enabled = !operationIsLoading
    )
    PasswordTextField(
        userPasswordInputState = userPasswordInputState,
        userPassword = userPassword,
        onValueChange = { onEvent(AccountViewModel.AccountEvent.OnPasswordInputChange(it)) },
        enabled = !operationIsLoading

    )
    PuppyPrimaryButton(
        buttonText = stringResource(id = R.string.get_started_button_text),
        onClick = { onEvent(AccountViewModel.AccountEvent.OnCreateUser) }
    )
}
