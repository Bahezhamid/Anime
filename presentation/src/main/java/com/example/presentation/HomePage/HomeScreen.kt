package com.example.presentation.HomePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.entity.FavoriteAnime
import com.example.presentation.AnimeBottomNavigationBar
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.BottomNavItem
import com.example.presentation.R

@Composable
fun HomeScreen(
    homePageViewModel: HomeScreenViewModel,
    onAnimeClicked : (Int) -> Unit,
    onSavedClicked : () -> Unit,
    onBookClicked : () -> Unit,
    onProfileClicked : () -> Unit,
    onPlayButtonClicked : (Int) -> Unit,
    onInfoButtonClicked : (Int) -> Unit,
    onSearchButtonClicked : () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        bottomBar = {
            AnimeBottomNavigationBar(
                selectedTab = BottomNavItem.Home,
                onTabSelected = {BottomNavItem.Home},
                onHomeClick = {},
                onSavedClick = onSavedClicked,
                onBookClick = onBookClicked,
                onProfileClick = onProfileClicked
            )

        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            when (val uiState = homePageViewModel.uiState.collectAsState().value) {
                is AnimeDataUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is AnimeDataUiState.Success -> AllAnimeScreen(
                    allAnimeData = uiState.animeData,
                    onAnimeClicked = onAnimeClicked,
                    onPlayButtonClicked = onPlayButtonClicked,
                    onInfoButtonClicked = onInfoButtonClicked,
                    isFirstAnimeInserted = homePageViewModel.isAnimeAddedToFavorite.collectAsState().value,
                    homePageViewModel = homePageViewModel,
                    onSearchButtonClicked = onSearchButtonClicked
                )
                is AnimeDataUiState.Error -> ErrorScreen(
                    homePageViewModel::getAnimeData,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading"
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "Loading Faild", modifier = Modifier.padding(16.dp))
        Button(
            onClick = retryAction,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = "retry")
        }
    }
}
@Composable
fun AllAnimeScreen(
    allAnimeData: AnimeDataWithPage?,
    onAnimeClicked: (Int) -> Unit,
    onPlayButtonClicked : (Int) -> Unit,
    onInfoButtonClicked : (Int) -> Unit,
    isFirstAnimeInserted: Boolean,
    homePageViewModel: HomeScreenViewModel,
    onSearchButtonClicked : () -> Unit,
) {
    val animeData =
        (homePageViewModel.uiState.collectAsState().value as AnimeDataUiState.Success).animeData
    LaunchedEffect(animeData) {
        animeData?.animeData?.first()?.id?.let {
            homePageViewModel.updateFavoriteStatus(
                animeId = it,
                userId = homePageViewModel.loginUiState.value.userid
            )
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
    ) {

        item (
            span = { GridItemSpan(3) }
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(560.dp)
            ) {

                AsyncImage(
                    model = ImageRequest
                        .Builder(context = LocalContext.current)
                        .data(allAnimeData?.animeData?.first()?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                AnimeTopAppBar(
                    title = "",
                    backGroundColor = Color.Transparent,
                    onSearchButtonClicked = onSearchButtonClicked
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.primary
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
                Column(modifier = Modifier.fillMaxSize()) {

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    val anime = allAnimeData?.animeData?.firstOrNull()
                                    if (anime != null) {
                                        if (isFirstAnimeInserted) {
                                            anime.id?.let {
                                                homePageViewModel.deleteAnimeFromFavorite(
                                                    animeId = it,
                                                    userId = homePageViewModel.loginUiState.value.userid
                                                )
                                            }
                                        } else {
                                            val favoriteAnime = anime.id?.let { id ->
                                                anime.imageUrl?.let { imageUrl ->
                                                    anime.title?.let { animeName ->
                                                        FavoriteAnime(
                                                            animeId = id,
                                                            animePoster = imageUrl,
                                                            animeName = animeName,
                                                            userId = homePageViewModel.loginUiState.value.userid
                                                        )
                                                    }
                                                }
                                            }
                                            favoriteAnime?.let {
                                                homePageViewModel.insertAnimeToFavorite(
                                                    favoriteAnime = it
                                                )
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (isFirstAnimeInserted) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite, contentDescription = "",
                                        modifier = Modifier.size(50.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(text = "Remove", style = MaterialTheme.typography.bodyLarge)
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "",
                                        modifier = Modifier.size(50.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(text = "Add", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                        Button(
                            onClick = { allAnimeData?.animeData?.first()?.id?.let { onPlayButtonClicked(it) } },
                            modifier = Modifier
                                .width(126.dp)
                                .height(56.dp)
                        ) {
                            Text(text = "Play", style = MaterialTheme.typography.headlineSmall)
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        allAnimeData?.animeData?.first()?.id?.let {
                                            onInfoButtonClicked(
                                                it
                                            )
                                        }
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "",
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Info",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                }
            }
        }
        item(
            span = { GridItemSpan(3) }
        ) {
            Text(text = "Top 10 Anime", style = MaterialTheme.typography.headlineMedium)
        }
        allAnimeData?.animeData?.let {
            items(it.filterNotNull()) { anime ->
                AnimeCard(
                    animeId = anime.id,
                    animeTitle = anime.title,
                    animePoster = anime.imageUrl,
                    onAnimeClicked = onAnimeClicked
                )
            }
        }

    }
}
@Composable
fun AnimeCard(
    animeId : Int?,
    animeTitle: String?,
    animePoster : String?,
    onAnimeClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = Modifier
            .padding(end = 10.dp, bottom = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable {
                if (animeId != null) {
                    onAnimeClicked(animeId)
                }
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        AsyncImage(model =ImageRequest
            .Builder(context = LocalContext.current)
            .data(animePoster)
            .crossfade(true)
            .build() ,
            contentDescription = "poster",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 190.dp)
                .clip(RoundedCornerShape(10.dp))
            ,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        Spacer(modifier = Modifier.height(5.dp))
        if (animeTitle != null) {
            Text(
                text = animeTitle,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}