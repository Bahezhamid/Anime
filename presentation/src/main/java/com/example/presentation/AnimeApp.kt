package com.example.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.HomePage.HomeScreen
import com.example.presentation.HomePage.HomeScreenViewModel
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpPage
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpViewModel
import com.example.presentation.navigation.AnimeScreen

@Composable
fun AnimeApp(
    navController: NavHostController = rememberNavController(),
) {
    val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
    val loginAndSignUpViewModel = hiltViewModel<LoginAndSignUpViewModel>()
    NavHost(
        navController = navController,
        startDestination = AnimeScreen.LogIn.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AnimeScreen.LogIn.route) {
            LoginAndSignUpPage(
                title = stringResource(R.string.welcome_back),
                description = stringResource(R.string.login_to_your_account),
                isSignUpPage = false,
                onButtonText = stringResource(R.string.login),
                authSwitchMessage = stringResource(R.string.don_t_have_an_account_signup_here),
                onBackPressed = { navController.navigateUp() },
                onAuthSwitchClick = { navController.navigate(AnimeScreen.SignUp.route) },
                onLoginAndSignUpButtonClicked = { navController.navigate(AnimeScreen.HomePage.route) },
                homePageViewModel = homeScreenViewModel,
                onForgetPasswordClicked = {navController.navigate(AnimeScreen.ForgetPasswordPage.route)},
                viewModel = loginAndSignUpViewModel
            )
        }
        composable(route = AnimeScreen.SignUp.route) {
            LoginAndSignUpPage(
                title = stringResource(id = R.string.signup),
                description = stringResource(R.string.create_an_account),
                isSignUpPage = true,
                onButtonText = stringResource(id = R.string.signup),
                authSwitchMessage = stringResource(R.string.already_have_an_account_login_here),
                onBackPressed = { navController.navigateUp() },
                onAuthSwitchClick = { navController.navigate(AnimeScreen.LogIn.route) },
                onLoginAndSignUpButtonClicked = { navController.navigate(AnimeScreen.HomePage.route) },
                homePageViewModel = homeScreenViewModel,
                viewModel = loginAndSignUpViewModel
            )
        }
        composable(route = AnimeScreen.ForgetPasswordPage.route) {
            LoginAndSignUpPage(
                title = "Forget Password?",
                description = "Enter your email to receive a password reset link.",
                isSignUpPage = false ,
                onButtonText = "Confirm",
                authSwitchMessage = stringResource(id = R.string.don_t_have_an_account_signup_here),
                onBackPressed = { navController.navigateUp() },
                onAuthSwitchClick = { navController.navigate(AnimeScreen.SignUp.route) },
                onLoginAndSignUpButtonClicked = { /*TODO*/ },
                viewModel = loginAndSignUpViewModel,
                isForgetPasswordPage = true,
                onForgetPasswordButtonClicked = {navController.navigate(AnimeScreen.LogIn.route)},
                homePageViewModel = homeScreenViewModel
            )
        }
        composable(route = AnimeScreen.HomePage.route) {
            HomeScreen(homeScreenViewModel = homeScreenViewModel)
        }
    }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeTopAppBar(
    title: String,
    isBackButton: Boolean = false,
    onBackButtonClicked : () -> Unit ={},
    onSearchButtonClicked : () -> Unit = {},
    backGroundColor : Color = MaterialTheme.colorScheme.secondary,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        navigationIcon = {
            if (isBackButton) {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
        },
        actions = {
            if (!isBackButton) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(25.dp)
                        .clickable { onSearchButtonClicked() }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =backGroundColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    )
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading"
    )
}