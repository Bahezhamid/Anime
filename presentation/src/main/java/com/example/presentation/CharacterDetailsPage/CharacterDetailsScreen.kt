package com.example.presentation.CharacterDetailsPage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.HomePage.ErrorScreen
import com.example.presentation.LoadingScreen
import com.example.presentation.R

@Composable
fun CharacterDetailsScreen(
    characterId : Int?,
    onBackButtonClicked : () -> Unit,
    onAnimeClicked : (Int) -> Unit,
    charactersDetailsViewModel: CharacterDetailsViewModel
) {
    Log.d("characterId",characterId.toString())
    LaunchedEffect(characterId) {
        characterId?.let {
            charactersDetailsViewModel.getCharactersDetail(it)
        }
    }
    Scaffold (
        topBar = {
            AnimeTopAppBar(
                title = "",
                isBackButton = true,
                onBackButtonClicked = onBackButtonClicked,
                backGroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ){ innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)

            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (charactersDetailsViewModel.charactersDetails.collectAsState().value) {
                is CharactersDetailsUiState.Loading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
                is CharactersDetailsUiState.Error -> ErrorScreen(
                    retryAction = {

                    },
                    modifier = Modifier.fillMaxSize()
                )

                is CharactersDetailsUiState.Success ->
                    CharacterDetailsScreen(
                        charactersDetailsViewModel = charactersDetailsViewModel,
                        onAnimeClicked = onAnimeClicked,
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }

    }
}

@Composable
fun CharacterDetailsScreen(
    charactersDetailsViewModel: CharacterDetailsViewModel,
    onAnimeClicked : (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val characterData = (charactersDetailsViewModel.charactersDetails.collectAsState()
        .value as CharactersDetailsUiState.Success).charactersDetails
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(context = LocalContext.current)
                        .data(characterData?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .heightIn(min = 150.dp, max = 180.dp)
                        .widthIn(min = 140.dp, max = 180.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                Spacer(modifier = Modifier.height(8.dp))
                characterData?.name?.let { name ->

                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }
        }



        item {
            Spacer(modifier = Modifier.height(5.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = characterData?.favorites.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }

        characterData?.description?.let { about ->
            item {
                Text(
                    text = about,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                text = "Voice Actor",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ){
                characterData?.listOfVoiceActor.let {
                    if (it != null) {
                        items(it){ actor ->
                            ActorCard(actorName = actor?.name,
                                actorImage = actor?.image,
                                actorLanguage = actor?.language,
                                modifier = Modifier
                                    .height(120.dp)
                                    .width(250.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.secondary)
                            )
                        }

                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                text = "Anime",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        characterData?.listOfAnime.let {
            if(it != null) {
                items(it) {anime ->
                    AnimeCard(animePoster = anime?.poster,
                        animeName = anime?.title,
                        characterRole = anime?.role ,
                        animeId = anime?.id,
                        onAnimeClicked = onAnimeClicked,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .height(120.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ActorCard(
    actorName : String?,
    actorImage : String?,
    actorLanguage : String?,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .clip(RoundedCornerShape(10.dp))
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(actorImage)
                .crossfade(true)
                .build(),
            contentDescription = "poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 10.dp)
        ) {
            if (actorName != null) {
                Text(
                    text = actorName,
                    style = MaterialTheme.typography.titleMedium
                )

            }
            Spacer(modifier = Modifier.height(10.dp))
            if (actorLanguage != null) {
                Text(
                    text = actorLanguage,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
@Composable
fun AnimeCard(
    animePoster : String?,
    animeName : String?,
    animeId : Int?,
    characterRole : String?,
    onAnimeClicked : (Int) -> Unit,
    modifier: Modifier
) {
    Row (
        modifier = modifier.clickable {
            if (animeId != null) {
                onAnimeClicked(animeId)
            }
        }
    ){
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(animePoster)
                .crossfade(true)
                .build(),
            contentDescription = "poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 10.dp)
        ) {
            if (animeName != null) {
                Text(
                    text = animeName,
                    style = MaterialTheme.typography.titleMedium
                )

            }
            Spacer(modifier = Modifier.height(10.dp))
            if (characterRole != null) {
                Text(
                    text = "Role:$characterRole",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}