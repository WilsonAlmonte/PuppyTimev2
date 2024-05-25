package com.codemote.puppytimev2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemote.puppytimev2.core.constants.ACCOUNT_SCREEN
import com.codemote.puppytimev2.core.constants.HOME_SCREEN
import com.codemote.puppytimev2.core.constants.LOADING_SCREEN
import com.codemote.puppytimev2.presentation.account.AccountScreen
import com.codemote.puppytimev2.presentation.home.HomeScreen
import com.codemote.puppytimev2.presentation.loading.LoadingScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = LOADING_SCREEN,
        modifier = modifier
    ) {
        composable(ACCOUNT_SCREEN) {
            AccountScreen()
        }
        composable(HOME_SCREEN) {
            HomeScreen()
        }
        composable(LOADING_SCREEN) {
            LoadingScreen()
        }
    }
}