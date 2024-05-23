package com.codemote.puppytimev2.presentation.account

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codemote.puppytimev2.R
import com.codemote.puppytimev2.presentation.account.components.AccountBottomSheet
import com.codemote.puppytimev2.presentation.account.components.ApplicationNameLabel
import com.codemote.puppytimev2.presentation.account.components.GetStartedButton
import com.codemote.puppytimev2.presentation.account.components.LoginLayout
import com.codemote.puppytimev2.presentation.account.components.SignInButton
import com.codemote.puppytimev2.presentation.account.components.SignUpLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = hiltViewModel<AccountViewModel>()
) {
    val accountUiState by accountViewModel.uiState.collectAsState()
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
                    SignInButton(onClick = {
                        accountViewModel.toggleAccountBottomSheet(
                            AccountBottomSheetType.LOGIN
                        )
                    })
                    GetStartedButton(onClick = {
                        accountViewModel.toggleAccountBottomSheet(
                            AccountBottomSheetType.SIGN_UP
                        )
                    })
                }
            }
        }
        AccountBottomSheet(
            accountUiState.showAccountBottomSheet,
            onDismiss = { accountViewModel.toggleAccountBottomSheet() },
            sheetState = sheetState
        ) {
            when (accountUiState.accountBottomSheetType) {
                AccountBottomSheetType.LOGIN -> {
                    LoginLayout(
                        userEmail = accountViewModel.userEmail,
                        userPassword = accountViewModel.userPassword,
                        onSignInClick = {
                            accountViewModel.authenticateUser {}
                        },
                        userEmailInputState = accountUiState.userEmailInputState,
                        userPasswordInputState = accountUiState.userPasswordInputState,
                        operationIsLoading = accountUiState.isLoading,
                        onValueChange = { value, type ->
                            accountViewModel.onInputChange(
                                value,
                                type
                            )
                        }
                    )
                }

                AccountBottomSheetType.SIGN_UP -> {
                    SignUpLayout(
                        userName = accountViewModel.userName,
                        userEmail = accountViewModel.userEmail,
                        userPassword = accountViewModel.userPassword,
                        userEmailInputState = accountUiState.userEmailInputState,
                        userPasswordInputState = accountUiState.userPasswordInputState,
                        onGetStartedClick = {},
                        operationIsLoading = accountUiState.isLoading,
                        onValueChange = { value, type ->
                            accountViewModel.onInputChange(
                                value,
                                type
                            )
                        }
                    )
                }

                AccountBottomSheetType.NONE -> {}
            }
        }
    }
}