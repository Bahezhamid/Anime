package com.example.presentation.SearchScreenPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.HomePage.AnimeCard
import com.example.presentation.HomePage.ErrorScreen
import com.example.presentation.LoadingScreen

@Composable
fun SearchScreen(
    onBackPressed : () -> Unit,
    onAnimeClicked : (Int) -> Unit,
    searchScreenViewModel: SearchScreenViewModel
) {
    val textFieldValue = searchScreenViewModel.searchTextFieldValue.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchedAnimeUiState = searchScreenViewModel.searchedAnimeUiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold (
        topBar = {
            AnimeTopAppBar(
                title = "",
                isBackButton = true,
                onBackButtonClicked = onBackPressed,
                backGroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = textFieldValue.value,
                onValueChange = {
                    searchScreenViewModel.updateSearchTextFieldValue(it)
                    searchScreenViewModel.getSearchedAnime(it)},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .focusRequester(focusRequester)
                ,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                keyboardOptions =   KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                placeholder = {
                    Text(
                        text = "Search for anime...",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        color = Color(0xff777777)
                    )
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        searchScreenViewModel.getSearchedAnime(textFieldValue.value)
                        keyboardController?.hide()
                    }
                )
            )
            when (searchedAnimeUiState.value) {
                is SearchedAnimeUiState.Loading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
                is SearchedAnimeUiState.Success -> {
                    val animeData = (searchedAnimeUiState.value as SearchedAnimeUiState.Success).SearchedAnime?.animeData
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            top = 20.dp,
                            end = 0.dp,
                            bottom = 10.dp,
                        ),
                    ) {
                        animeData.let {
                            if (it != null) {
                                items(it) {anime ->
                                    AnimeCard(animeId = anime?.id,
                                        animeTitle = anime?.title,
                                        animePoster = anime?.imageUrl,
                                        onAnimeClicked = onAnimeClicked,
                                    )
                                }
                            }
                        }
                    }
                }
                is SearchedAnimeUiState.Error -> ErrorScreen(
                    retryAction = { searchScreenViewModel.getSearchedAnime(animeName = textFieldValue.value) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
