package com.example.presentation.AnimeChapterPage

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.entity.AnimeChaptersData
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.HomePage.ErrorScreen
import com.example.presentation.LoadingScreen
import com.example.presentation.R

@Composable
fun AnimeChaptersScreen(
    animeId : Int,
    onBackButtonClicked : () -> Unit,
    animeChaptersViewModel: AnimeChaptersViewModel
) {
    LaunchedEffect(animeId) {
        animeId.let {
            animeChaptersViewModel.getAllChapters(id = it, page = 1)
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
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (animeChaptersViewModel.animeChaptersUiState.collectAsState().value) {
                is AnimeChaptersUiState.Loading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
                is AnimeChaptersUiState.Error -> ErrorScreen(
                    retryAction = {

                    },
                    modifier = Modifier.fillMaxSize()
                )

                is AnimeChaptersUiState.Success ->
                    AnimeChaptersPage(
                        animeId = animeId,
                        modifier = Modifier.fillMaxSize(),
                        animeChaptersViewModel = animeChaptersViewModel
                    )
            }
        }

    }
}

@Composable
fun AnimeChaptersPage(
    animeId : Int ,
    modifier: Modifier,
    animeChaptersViewModel: AnimeChaptersViewModel
) {
    val currentPage = animeChaptersViewModel.currentPage.collectAsState()
    val chapters =  (animeChaptersViewModel.animeChaptersUiState.collectAsState()
        .value as AnimeChaptersUiState.Success).animeChapters
    if(chapters?.listOfChapters.isNullOrEmpty()){
        Text(
            text = "No episodes available",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
    } else {
        chapters?.page?.let {
            PageNavigationRow(
                currentPage =currentPage.value,
                totalPages = it,
                onPageClick ={ page ->
                    animeChaptersViewModel.updateCurrentPage(page)
                    animeChaptersViewModel.getAllChapters(id = animeId,page = page)
                },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            chapters?.listOfChapters?.let {
                items(it) { chapter ->
                    ChapterCard(
                        chapter = chapter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp, max = 140.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun ChapterCard(
    chapter : AnimeChaptersData?,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val urlToOpen = chapter?.url

    Row (
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                uriHandler.openUri(Uri.parse(urlToOpen).toString())
            }
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(model = ImageRequest
            .Builder(context = LocalContext.current)
            .data(chapter?.imageUrl)
            .crossfade(true)
            .build() ,
            contentDescription = "poster",
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
            ,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp),
            verticalArrangement = Arrangement.Center
        ) {
            chapter?.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                ) }
            Spacer(modifier = Modifier.height(10.dp))
            chapter?.episodeNumber?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                ) }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.PlayArrow,
            contentDescription = "",
            Modifier.size(50.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun PageNavigationRow(
    currentPage: Int,
    totalPages: Int? = 1,
    onPageClick: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items((1..totalPages!!).toList()) { page ->
            TextButton(
                onClick = { onPageClick(page) },
                modifier = Modifier

                    .padding(horizontal = 4.dp)
                    .border(
                        width = 1.dp,
                        color = if (page == currentPage) MaterialTheme.colorScheme.secondary else Color.Gray,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = page.toString(),
                    color = if (page == currentPage) MaterialTheme.colorScheme.secondary else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
