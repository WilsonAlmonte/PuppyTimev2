package com.codemote.puppytimev2.presentation.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.presentation.account.components.AccountBottomSheet
import com.codemote.puppytimev2.presentation.account.components.ApplicationNameLabel
import com.codemote.puppytimev2.presentation.account.components.GetStartedButton
import com.codemote.puppytimev2.presentation.account.components.LoginButton
import com.codemote.puppytimev2.presentation.account.components.LoginLayout
import com.codemote.puppytimev2.presentation.account.components.SignUpLayout

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel<AccountViewModel>()
) {
    val accountUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    AccountScreen(
        modifier = modifier,
        accountUiState = accountUiState,
        userEmail = viewModel.userEmail,
        userPassword = viewModel.userPassword,
        userName = viewModel.userName,
        onEvent = { viewModel.processEvent(it) }
    )
    LaunchedEffect(accountUiState.effect) {
        when (accountUiState.effect) {
            is AccountViewModel.AccountEffect.ShowToast -> {
                Toast.makeText(
                    context,
                    (accountUiState.effect as AccountViewModel.AccountEffect.ShowToast).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            null -> Unit
        }
        viewModel.processEvent(AccountViewModel.AccountEvent.ConsumeEffect)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    accountUiState: AccountViewModel.AccountUiState,
    modifier: Modifier = Modifier,
    userEmail: String,
    userPassword: String,
    userName: String,
    onEvent: (AccountViewModel.AccountEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Surface {
        Box(
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.background
                )
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_bk),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1.5f),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ApplicationNameLabel()
                }
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    LoginButton(onClick = { onEvent(AccountViewModel.AccountEvent.ShowLoginBottomSheet) })
                    GetStartedButton(onClick = { onEvent(AccountViewModel.AccountEvent.ShowSignUpBottomSheet) })
                }
            }
        }
        AccountBottomSheet(
            accountUiState.showAccountBottomSheet,
            onDismiss = { onEvent(AccountViewModel.AccountEvent.HideAccountBottomSheet) },
            sheetState = sheetState,
        ) {
            when (accountUiState.accountBottomSheetType) {
                AccountBottomSheetType.LOGIN -> {
                    LoginLayout(
                        userEmail = userEmail,
                        userPassword = userPassword,
                        userEmailInputState = accountUiState.userEmailInputState,
                        userPasswordInputState = accountUiState.userPasswordInputState,
                        operationIsLoading = accountUiState.isLoading,
                        onEvent = onEvent
                    )
                }

                AccountBottomSheetType.SIGN_UP -> {
                    SignUpLayout(
                        userName = userName,
                        userEmail = userEmail,
                        userPassword = userPassword,
                        userEmailInputState = accountUiState.userEmailInputState,
                        userPasswordInputState = accountUiState.userPasswordInputState,
                        operationIsLoading = accountUiState.isLoading,
                        onEvent = onEvent,
                    )
                }

                AccountBottomSheetType.NONE -> {}
            }
        }
    }
}