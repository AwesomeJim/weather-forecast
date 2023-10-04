package com.awesomejim.weatherforecast.di.flickr

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class FlickrResponse(
    @SerialName("photos") val results: FlickerResultsResponse? = null,
    @SerialName("stat") val status: String,
    @SerialName("message") val message: String? = null
)

@Serializable
data class FlickerResultsResponse(
    @SerialName("page") val page: Int,
    @SerialName("pages") val pages: Int,
    @SerialName("perpage") val perpage: Int,
    @SerialName("total") val total: Int,
    @SerialName("photo") val photos: List<FlickerPhotoResponse>,
)

@Serializable
data class FlickerPhotoResponse(
    @SerialName("id") val photoId: String,
    @SerialName("title") val photoTitle: String,
    @SerialName("url_m") val photoUrl: String? = null
)


fun List<FlickerPhotoResponse>.toCleanPhotos():List<FlickerPhotoResponse> {
     return this.map {
          it.toCoreModule()
      }
}

/**
 * To core module - This is a fix to curb the issue of lazy column from crashing for having same item id
 *
 * @return
 */
fun FlickerPhotoResponse.toCoreModule():FlickerPhotoResponse =
    FlickerPhotoResponse(
        photoId = this.photoId + Random.nextInt(),
        photoTitle = this.photoTitle,
        photoUrl = this.photoUrl

    )