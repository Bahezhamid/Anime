package com.example.presentation.HomePage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel
) {
    val uiState = homeScreenViewModel.uiState.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        uiState.value?.animeData?.first()?.title?.let { Text(text = it) }
    }
}