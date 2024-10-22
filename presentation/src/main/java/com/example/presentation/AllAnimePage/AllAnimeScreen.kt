package com.example.presentation.AllAnimePage

import android.content.ClipData.Item
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.entity.AllGenreData
import com.example.presentation.AnimeBottomNavigationBar
import com.example.presentation.AnimeChapterPage.PageNavigationRow
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.BottomNavItem
import com.example.presentation.HomePage.AnimeCard
import com.example.presentation.HomePage.ErrorScreen
import com.example.presentation.LoadingScreen

@Composable
fun AllAnimeScreen(
    allAnimeViewScreenModel: AllAnimeScreenViewModel,
    onHomeButtonClicked : () -> Unit,
    onSavedButtonClicked : () -> Unit,
    onProfileClicked : () -> Unit,
    onAnimeClicked : (Int) -> Unit,
    onSearchButtonClicked : () -> Unit,

    ) {
    val allGenresUiState by allAnimeViewScreenModel.allGenresUiState.collectAsState()
    val allSelectedAnimeByGenreState by allAnimeViewScreenModel.allSelectedAnimeByGenre.collectAsState()
    Scaffold (
        topBar = {
            AnimeTopAppBar(
                title = "Browse",
                onSearchButtonClicked = onSearchButtonClicked
            )
        },
        bottomBar = {
            AnimeBottomNavigationBar(
                selectedTab = BottomNavItem.Book,
                onTabSelected = { BottomNavItem.Book},
                onHomeClick = onHomeButtonClicked,
                onSavedClick = onSavedButtonClicked,
                onBookClick = {},
                onProfileClick = onProfileClicked
            )

        },
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.primary)
        ){
            when (allGenresUiState) {
                is AllGenreUiState.Loading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
                is AllGenreUiState.Success -> {
                    val genres = (allGenresUiState as AllGenreUiState.Success).genres
                    AllAnimeScreen(
                        allGenres = genres,
                        onGenreClicked = { genre ->
                            if (genre != null) {
                                allAnimeViewScreenModel.getAnimeByGenre()
                                allAnimeViewScreenModel.updateCurrentPage(1)
                            }
                        },
                        allAnimeSelectedByGenre = allSelectedAnimeByGenreState,
                        onAnimeClicked = onAnimeClicked,
                        allAnimeSelected = {allAnimeViewScreenModel.fetchAllData()},
                        allAnimeViewScreenModel = allAnimeViewScreenModel
                    )
                }
                is AllGenreUiState.Error -> ErrorScreen(
                    retryAction = allAnimeViewScreenModel::fetchAllData,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AllAnimeScreen(
    allGenres: List<AllGenreData?>?,
    allAnimeSelected : () -> Unit,
    onGenreClicked: (Int?) -> Unit,
    allAnimeSelectedByGenre: AllAnimeByGenreUiState,
    onAnimeClicked: (Int) -> Unit,
    allAnimeViewScreenModel: AllAnimeScreenViewModel
) {
    var selectedGenre = allAnimeViewScreenModel.selectedGenre.collectAsState().value
    val currentPage = allAnimeViewScreenModel.currentPage.collectAsState()
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    ) {
        item {
            GenreChip(genre = "All",
                isSelected = selectedGenre == 0,
                onClick = {
                    allAnimeViewScreenModel.updateSelectedGenre(0)
                    onGenreClicked(selectedGenre)
                }
            )
        }
        allGenres?.let {
            items(it) { genre ->
                GenreChip(
                    genre = genre?.title,
                    isSelected = selectedGenre == genre?.id,
                    onClick = {
                        allAnimeViewScreenModel.updateSelectedGenre(genre?.id!!)
                        onGenreClicked(selectedGenre)
                    }
                )
            }
        }
    }

    when(allAnimeSelectedByGenre) {
        is AllAnimeByGenreUiState.Loading -> LoadingScreen(
            modifier = Modifier.fillMaxSize()
        )
        is AllAnimeByGenreUiState.Error -> ErrorScreen(
            retryAction = { onGenreClicked(selectedGenre) },
            modifier = Modifier.fillMaxSize()
        )
        is AllAnimeByGenreUiState.Success -> {
            val allAnimeByGenre
                    = (allAnimeSelectedByGenre as AllAnimeByGenreUiState.Success).animeList

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
                items(allAnimeByGenre?.animeData!!.filterNotNull()) { anime ->
                    AnimeCard(animeId = anime.id,
                        animeTitle = anime.title,
                        animePoster = anime.imageUrl,
                        onAnimeClicked = onAnimeClicked,
                    )
                }
                item (  span = { GridItemSpan(3) }){
                    allAnimeByGenre.page.let {
                        PageNavigationRow(
                            currentPage =currentPage.value,
                            totalPages = it,
                            onPageClick ={ page ->
                                allAnimeViewScreenModel.updateCurrentPage(page)
                                selectedGenre.let { it1 -> allAnimeViewScreenModel.getAnimeByGenre() }
                            },
                        )
                    }
                }
            }

        }
    }

}
@Composable
fun GenreChip(
    genre: String?,
    isSelected : Boolean,
    onClick: () -> Unit)
{
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (genre != null) {
            Text(text = genre, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
