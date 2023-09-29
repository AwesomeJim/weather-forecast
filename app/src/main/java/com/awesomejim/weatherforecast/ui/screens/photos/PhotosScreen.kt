package com.awesomejim.weatherforecast.ui.screens.photos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.flickr.FlickerPhotoResponse
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import timber.log.Timber


@Composable
fun PhotosScreen(
    photosViewModel: PhotosViewModel,
    defaultLocation: DefaultLocation
) {
    val photosList = photosViewModel.getLocationPhotos(
        DefaultLocation(
            longitude = defaultLocation.longitude,
            latitude = defaultLocation.latitude
        )
    ).collectAsLazyPagingItems()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(
                count = photosList.itemCount,
                key = photosList.itemKey { it.photoId }
            ) { index ->
                val photo = photosList[index]
                photo?.let { validPhoto ->
                    validPhoto.photoUrl?.let { valid ->
                        PhotoCard(photo = validPhoto)
                    }
                }
            }
            when (val state = photosList.loadState.refresh) { //FIRST LOAD
                is LoadState.Error -> {
                    //state.error to get error message
                    Timber.tag("PhotosScreen").e(state.error)
                }

                is LoadState.Loading -> { // Loading UI
                    item {
                        PhotosLoadingScreen(
                            modifier = Modifier
                                .size(200.dp)
                        )

                    }
                }

                else -> {}
            }
            when (val state = photosList.loadState.append) { // Pagination
                is LoadState.Error -> {
                    //state.error to get error message
                    Timber.tag("PhotosScreen").e(state.error)
                    item {
                        PhotosErrorScreen(retryAction = {}, modifier = Modifier.fillMaxSize())
                    }
                }

                is LoadState.Loading -> { // Pagination Loading UI
                    item {
                        PhotosLoadingScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .size(200.dp)
                        )
                    }
                }

                else -> {}
            }
        },
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 64.dp)
    )
}

@Composable
fun PhotoCard(photo: FlickerPhotoResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(photo.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = photo.photoTitle,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
            Text(
                text = photo.photoTitle,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun PhotosLoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LoadingProgressScreens()
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun PhotosErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    WeatherForecastTheme {
        PhotosLoadingScreen(
            Modifier
                .fillMaxSize()
                .size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    WeatherForecastTheme {
        PhotosErrorScreen({}, Modifier.fillMaxSize())
    }
}