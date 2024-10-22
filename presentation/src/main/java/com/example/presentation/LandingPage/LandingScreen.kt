package com.example.presentation.LandingPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.HomePage.HomeScreenViewModel
import com.example.presentation.LoadingScreen
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpViewModel
import com.example.presentation.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(
    onLoginButtonClicked : () -> Unit,
    onSignButtonClicked : () -> Unit,
    loginAndSignUpViewModel: LoginAndSignUpViewModel,
    homePageViewModel: HomeScreenViewModel,
    onLoginAndSignUpButtonClicked : () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginUiState = loginAndSignUpViewModel.loginUiState.collectAsState().value
    val isLoading = loginUiState.isLoading
    val isSuccess = loginUiState.isSuccess
    LaunchedEffect(loginUiState) {
        if (loginUiState.isSuccess) {
            homePageViewModel.updateUserUiState(loginUiState)
            onLoginAndSignUpButtonClicked()
        }
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.anime_posters),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isLoading){
                LoadingScreen(modifier = Modifier.fillMaxSize())
            } else if(!isSuccess) {
                Spacer(modifier = Modifier.height(250.dp))
                Text(
                    text = stringResource(R.string.otaku_hub),
                    style = MaterialTheme.typography.headlineLarge
                        .copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,

                    )
                Spacer(modifier = Modifier.height(164.dp))
                Button(
                    onClick = onLoginButtonClicked,
                    modifier = Modifier
                        .width(232.dp)
                        .height(61.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.height(22.dp))
                Button(
                    onClick = onSignButtonClicked,
                    modifier = Modifier
                        .width(232.dp)
                        .height(61.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.signup),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

}
