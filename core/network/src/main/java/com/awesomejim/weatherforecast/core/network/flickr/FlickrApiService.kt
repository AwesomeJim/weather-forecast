package com.awesomejim.weatherforecast.core.network.flickr

import com.awesomejim.weatherforecast.core.network.NETWORK_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {
    @GET("?")
    suspend fun getLocationPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("radius") radius: Int = 20,
        @Query("per_page") per_page: Int = NETWORK_PAGE_SIZE,
        @Query("extras") extras: String = "url_m",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1,
        @Query("api_key") api_key: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("page") page: Int
    ): Response<FlickrResponse>
}
