package com.codemote.puppytimev2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.codemote.puppytimev2.core.AuthState
import com.codemote.puppytimev2.core.constants.ACCOUNT_SCREEN
import com.codemote.puppytimev2.core.constants.HOME_SCREEN
import com.codemote.puppytimev2.presentation.loading.LoadingScreen
import com.codemote.puppytimev2.ui.navigation.NavigationGraph
import com.codemote.puppytimev2.ui.theme.PuppyTimeV2Theme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val mainViewModel = hiltViewModel<MainViewModel>()
            val snackbarHostState = remember { SnackbarHostState() }
            val userSignedState by mainViewModel.userSignedState.collectAsState()
            PuppyTimeV2Theme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(it),
                    )
                    when (userSignedState) {
                        AuthState.SIGNED_IN -> {
                            navController.navigate(HOME_SCREEN) {
                                popUpTo(navController.graph.id)
                            }
                        }

                        AuthState.SIGNED_OUT -> {
                            navController.navigate(ACCOUNT_SCREEN) {
                                popUpTo(navController.graph.id)
                            }
                        }

                        AuthState.LOADING -> {
                            LoadingScreen()
                        }
                    }
                }
            }
        }
    }
}