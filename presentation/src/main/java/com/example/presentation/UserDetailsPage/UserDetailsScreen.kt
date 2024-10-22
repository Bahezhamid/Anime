package com.example.presentation.UserDetailsPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.LoginAndSignUpPage.UsersUiState

@Composable
fun UserDetainsScreen(
    onBackButtonClicked : () -> Unit,
    userData : UsersUiState,
    userDetailsViewModel: UserDetailsViewModel,
) {
    LaunchedEffect(userData.userid) {
        userDetailsViewModel.getFavoriteCount(userId = userData.userid)
    }
    Scaffold(
        topBar = {
            AnimeTopAppBar(
                title = "",
                isBackButton = true,
                onBackButtonClicked = onBackButtonClicked,
                backGroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { innerPadding ->
        Column (
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Your Data",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 10.dp)

                ,
                verticalArrangement = Arrangement.SpaceAround,
            ){
                Text(
                    text = "Your User Name :",
                    style = MaterialTheme.typography.titleLarge
                )
                userData.userName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Your Email :",
                    style = MaterialTheme.typography.titleLarge
                )
                userData.email?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Your Favorite Anime : ${userDetailsViewModel.favoriteCount.collectAsState().value}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}