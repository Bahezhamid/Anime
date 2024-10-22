package com.example.presentation.AnimeDetailsPage

import android.net.Uri
import android.util.Log
import android.webkit.WebView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.entity.AnimeAllCharactersData
import com.example.domain.entity.AnimeDataById
import com.example.domain.entity.FavoriteAnime
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.HomePage.ErrorScreen
import com.example.presentation.HomePage.HomeScreenViewModel
import com.example.presentation.LoadingScreen
import com.example.presentation.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    animeId : Int?,
    animeDetailsViewModel: AnimeDetailsViewModel,
    onBackPressed : () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    onPlayButtonClicked : (Int) -> Unit,
    onShareButtonClicked : (String,String , String) -> Unit,
    homePageViewModel: HomeScreenViewModel
) {
    LaunchedEffect(animeId) {
        animeId?.let {
            animeDetailsViewModel.getAnimeDataById(it)
        }
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
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (animeDetailsViewModel.animeDataByIdUiState.collectAsState().value) {
                is AnimeDetailsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is AnimeDetailsUiState.Error -> ErrorScreen(
                    retryAction = {
                        if (animeId != null) {
                            animeDetailsViewModel.getAnimeDataById(animeId)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                is AnimeDetailsUiState.Success ->
                    AnimeDetailsScreen(
                        allAnimeDetails =
                        (animeDetailsViewModel.animeDataByIdUiState.collectAsState()
                            .value as AnimeDetailsUiState.Success).animeDetails,
                        animeCharacters = (animeDetailsViewModel.animeDataByIdUiState.collectAsState()
                            .value as AnimeDetailsUiState.Success).animeCharacters,
                        onCharacterClicked = onCharacterClicked ,
                        onPlayButtonClicked = onPlayButtonClicked,
                        homePageViewModel = homePageViewModel,
                        onShareButtonClicked = onShareButtonClicked
                    )
            }

        }
    }
}

@Composable
fun AnimeDetailsScreen(
    allAnimeDetails : AnimeDataById?,
    animeCharacters : List<AnimeAllCharactersData?>?,
    onCharacterClicked: (Int) -> Unit,
    onPlayButtonClicked : (Int) -> Unit,
    onShareButtonClicked: (String, String ,String) -> Unit,
    homePageViewModel : HomeScreenViewModel,
) {
    val isAnimeAddedToFavorite = homePageViewModel.isAnimeAddedToFavorite.collectAsState()
    LaunchedEffect (allAnimeDetails){
        allAnimeDetails?.id?.let { homePageViewModel.updateFavoriteStatus(it, userId = homePageViewModel.loginUiState.value.userid) }
    }
    val genresList = allAnimeDetails?.genre?.map { it } ?: emptyList()
    val genresText = genresList.joinToString(separator = ", ")

    Spacer(modifier = Modifier.height(16.dp))
    val animePoster = (allAnimeDetails?.trailerImg
        ?.takeIf { it.isNotBlank() }
        ?: allAnimeDetails?.imageUrl)
    val uriHandler = LocalUriHandler.current
    val urlToOpen = allAnimeDetails?.trailerUrl
    Box(modifier = Modifier
        .padding(horizontal = 19.dp)
        .fillMaxWidth()
        .heightIn(min = 120.dp, max = 198.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable(enabled = !urlToOpen.isNullOrEmpty()) {
            uriHandler.openUri(
                Uri.parse(urlToOpen).toString()
            )
        }) {
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(animePoster)
                .crossfade(true)
                .build(),
            contentDescription = "poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        if(!urlToOpen.isNullOrEmpty()) {
            Icon( imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center))
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    allAnimeDetails?.title?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row (
        modifier = Modifier
            .padding(horizontal = 19.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "About Anime",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 19.dp)
            .heightIn(max = 250.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .clip(RoundedCornerShape(20.dp))
            .verticalScroll(rememberScrollState())
            .background(
                MaterialTheme.colorScheme.secondary
            )
            .padding(10.dp)
    ) {

        Text(
            text = "Released Date: ${extractYearFromDate(allAnimeDetails?.releaseDate)}",
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Popularity Rank: ${allAnimeDetails?.popularityRank}",
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Global Ranked: ${allAnimeDetails?.globalRank}",
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Genre: $genresText",
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Episodes:${allAnimeDetails?.episodes}(${allAnimeDetails?.episodesMin})",
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Description:",
            color = MaterialTheme.colorScheme.onPrimary
        )

        allAnimeDetails?.description?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onPrimary,
                style = androidx.compose.ui.text.TextStyle(

                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start,
                )
            )
        }

    }
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        animeCharacters?.let {
            items(it.filterNotNull()) { character ->
                CharacterCard(
                    characterImage = character.imageUrl,
                    characterId = character.id,
                    onCharacterClicked = onCharacterClicked
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    Button(
        onClick = { allAnimeDetails?.id?.let { onPlayButtonClicked(it) } },
        modifier = Modifier
            .padding(horizontal = 19.dp)
            .width(200.dp)
            .height(80.dp)
        ,

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text = "Play", style = MaterialTheme.typography.headlineSmall)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .padding(horizontal = 19.dp)
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if(isAnimeAddedToFavorite.value) {
            IconsFunction(imageVector = Icons.Default.Favorite,
                iconName = "Remove",
                isAddedToFavorite = true,
                homePageViewModel = homePageViewModel,
                allAnimeDetails = allAnimeDetails
            )
        }
        else {
            IconsFunction(
                imageVector = Icons.Default.FavoriteBorder,
                iconName = "Add",
                isAddedToFavorite = false,
                homePageViewModel = homePageViewModel,
                allAnimeDetails = allAnimeDetails
            )
        }
        IconsFunction(
            imageVector = Icons.Default.Share, iconName = "Share",
            isShareButton = true,
            homePageViewModel = homePageViewModel,
            allAnimeDetails = allAnimeDetails,
            onShareButtonClicked = onShareButtonClicked
        )
    }
}

@Composable
fun IconsFunction(
    imageVector: ImageVector,
    iconName : String,
    allAnimeDetails : AnimeDataById?,
    isAddedToFavorite : Boolean = false,
    isShareButton : Boolean =false,
    onShareButtonClicked: (String, String , String) -> Unit = { _, _ , _ -> },
    homePageViewModel: HomeScreenViewModel
) {
    val animeSharingDetails  = "Anime Name: ${allAnimeDetails?.title}\n" +
            "Episodes :${allAnimeDetails?.episodes} (${allAnimeDetails?.episodesMin})\n" +
            "About Anime: ${allAnimeDetails?.description}"
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable {
                if (isShareButton) {
                    allAnimeDetails?.animeUrl?.let {
                        onShareButtonClicked(
                            "Sharing Anime", animeSharingDetails,
                            it
                        )
                    }
                } else if (
                    isAddedToFavorite
                ) {

                    allAnimeDetails?.id?.let {
                        homePageViewModel.deleteAnimeFromFavorite(
                            animeId = it
                        )
                    }
                } else {
                    allAnimeDetails?.id
                        ?.let {
                            allAnimeDetails.title?.let { it1 ->
                                allAnimeDetails.imageUrl?.let { it2 ->
                                    FavoriteAnime(
                                        animeId = it,
                                        animeName = it1,
                                        animePoster = it2,
                                        userId = homePageViewModel.loginUiState.value.userid
                                    )
                                }
                            }
                        }
                        ?.let { homePageViewModel.insertAnimeToFavorite(favoriteAnime = it) }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = iconName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
@Composable
fun CharacterCard(
    characterImage: String?,
    characterId : Int?,
    onCharacterClicked : (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("charactermmmm",characterId.toString())
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable {
                if (characterId != null) {
                    onCharacterClicked(characterId)
                }
            }
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(characterImage)
                .crossfade(true)
                .build(),
            contentDescription = "Character Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img)
        )
    }
}

fun extractYearFromDate(dateString: String?): String {
    return dateString?.takeIf { it.length >= 4 }?.substring(0, 4) ?: ""
}