package com.awesomejim.weatherforecast.feature.bookmarks.maps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import timber.log.Timber

/**
 * Created by Awesome Jim on.
 * 03/10/2023
 */

var myCurrentLocation = LatLng(1.3588227, 103.8742114)

private const val TAG = "MapsViewScreen"


@Composable
fun MapsViewScreen(
    modifier: Modifier = Modifier,
    viewModel: MapsViewModel
) {
    //
    val savedLocationListUiState = viewModel
        .savedLocationListUiState
        .collectAsStateWithLifecycle().value
    val defaultLocation = viewModel.currentLocation

    //
    myCurrentLocation = LatLng(
        defaultLocation.latitude,
        defaultLocation.longitude
    )
    //
    if (savedLocationListUiState.itemList.isNotEmpty()) {
        MapsViewContent(savedLocationListUiState.itemList, modifier.fillMaxSize())
    }
}

@Composable
fun MapsViewContent(
    exploreList: List<com.awesomejim.weatherforecast.core.LocationItemData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center
    ) {
        GoogleMapCreateClusters(exploreList)
    }
}

@Composable
fun GoogleMapCreateClusters(exploreList: List<com.awesomejim.weatherforecast.core.LocationItemData>) {
    val items = remember { mutableStateListOf<LocationItem>() }
    LaunchedEffect(Unit) {
        for (item in exploreList) {
            val position = LatLng(
                item.locationLatitude,
                item.locationLongitude
            )
            val weatherTemp = "%1\$1.0f \u00B0 %2\$s".format(
                item.locationWeatherInfo.weatherTemp,
                item.locationWeatherInfo.weatherConditionDescription
            )
            items.add(LocationItem(position, item.locationName, weatherTemp, 9f))
        }
    }
    GoogleMapClustering(items = items)
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(items: List<LocationItem>) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                // To show blue dot on map
                isMyLocationEnabled = true
            )
        )
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            properties = properties,
            onMapLoaded = {
                isMapLoaded = true
            },
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(myCurrentLocation, 10f)
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
                state = rememberMarkerState(position = myCurrentLocation),
                onClick = {
                    Timber.tag(TAG).d("Non-cluster marker clicked! %s", it)
                    true
                }
            )
        }
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentSize()
                )
            }
        }
    }
}

data class LocationItem(
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

    override fun getZIndex(): Float? {
        TODO("Not yet implemented")
    }

}
