/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awesomejim.weatherforecast.ui.screens.maps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

var singapore = LatLng(1.3588227, 103.8742114)
fun launchDetailsActivity(context: Context) {
    context.startActivity(createDetailsActivityIntent(context))
}

@VisibleForTesting
fun createDetailsActivityIntent(context: Context): Intent {
    return Intent(context, MapsViewActivity::class.java)
}

private val TAG = "MapsViewActivity"
@AndroidEntryPoint
class MapsViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WeatherForecastTheme {
                Surface {
                    DetailsScreen(
                        onErrorLoading = { },
                        modifier = Modifier.systemBarsPadding()
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    onErrorLoading: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapsViewModel = viewModel()
) {
    val savedLocationListUiState = viewModel
        .savedLocationListUiState
        .collectAsStateWithLifecycle().value

    // when {
    if (savedLocationListUiState.itemList.isNotEmpty()) {
        DetailsContent(savedLocationListUiState.itemList, modifier.fillMaxSize())
    }

//        uiState.isLoading -> {
//            Box(modifier.fillMaxSize()) {
//                CircularProgressIndicator(
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//        }

    //   else -> {
    // onErrorLoading()
    //   }
    //}
}

@Composable
fun DetailsContent(
    exploreList: List<LocationItemData>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer), verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(32.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Saved Location",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Spacer(Modifier.height(16.dp))
        singapore = LatLng(
            exploreList[0].locationLatitude,
            exploreList[0].locationLongitude
        )
        GoogleMapCreateClusters(exploreList)
    }
}

@Composable
fun GoogleMapCreateClusters(exploreList: List<LocationItemData>) {
    val items = remember { mutableStateListOf<MyItem>() }
    LaunchedEffect(Unit) {
        for (item in exploreList) {
            val position = LatLng(
                item.locationLatitude,
                item.locationLongitude
            )
             val weatherTemp  = "%1\$1.0f \u00B0 %2\$s".format(item.locationWeatherInfo.weatherTemp,item.locationWeatherInfo.weatherConditionDescription)
            items.add(MyItem(position, item.locationName, weatherTemp, 9f))
        }
    }
    GoogleMapClustering(items = items)
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(items: List<MyItem>) {
    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false, myLocationButtonEnabled = true)) }
    GoogleMap(
        uiSettings = uiSettings,
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
    ) {
        Clustering(
            items = items,
            // Optional: Handle clicks on clusters, cluster items, and cluster item info windows
            onClusterClick = {
                Timber.tag(TAG).d("Cluster clicked! %s", it)
                false
            },
            onClusterItemClick = {
                Timber.tag(TAG).d("Cluster item clicked! %s", it)
                false
            },
            onClusterItemInfoWindowClick = {
                Timber.tag(TAG).d("Cluster item info window clicked! %s", it)
            },
            // Optional: Custom rendering for clusters
            clusterContent = { cluster ->
                Surface(
                    Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Blue,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "%,d".format(cluster.size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            // Optional: Custom rendering for non-clustered items
            clusterItemContent = null
        )
        MarkerInfoWindow(
            state = rememberMarkerState(position = singapore),
            onClick = {
                Timber.tag(TAG).d("Non-cluster marker clicked! %s", it)
                true
            }
        )
    }
}

data class MyItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    fun getZIndex(): Float =
        itemZIndex
}
