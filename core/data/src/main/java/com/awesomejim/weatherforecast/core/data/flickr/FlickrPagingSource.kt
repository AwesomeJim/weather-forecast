package com.awesomejim.weatherforecast.core.data.flickr


import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.awesomejim.weatherforecast.core.FlickerPhoto

import com.awesomejim.weatherforecast.core.GenericException
import com.awesomejim.weatherforecast.core.data.source.mapper.toCleanPhotos
import com.awesomejim.weatherforecast.core.network.BuildConfig
import com.awesomejim.weatherforecast.core.network.NETWORK_PAGE_SIZE
import com.awesomejim.weatherforecast.core.network.flickr.FlickrApiService


import timber.log.Timber
import java.io.IOException

const val FLICKR_STARTING_PAGE_INDEX = 1
class FlickrPagingSource(
    private val newsApiService: FlickrApiService,
    private val defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation
) : PagingSource<Int, FlickerPhoto>() {

    var errorMessage: String = "An error occurred please try again later"

    override fun getRefreshKey(state: PagingState<Int, FlickerPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlickerPhoto> {
        return try {
            val page = params.key ?: FLICKR_STARTING_PAGE_INDEX
            Timber.tag("Photos PagingSource").e("load init: $page $defaultLocation")
            val response = newsApiService.getLocationPhotos(
                page = page,
                api_key = BuildConfig.FLICKR_API_KEY,
                lat = defaultLocation.latitude,
                lon = defaultLocation.longitude
            )
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.status == "fail") {
                    errorMessage =
                        response.body()!!.message ?: "An error occurred please try again later"

                    val throwable = GenericException(message = errorMessage)
                    LoadResult.Error(throwable)
                } else {
                    Timber.tag("Photos PagingSource")
                        .e("response init: ${response.body()!!.results?.photos}")
                    val photoResponse = response.body()!!.results?.photos ?: emptyList()
                    val nextKey = if (photoResponse.isEmpty()) {
                        null
                    } else {
                        // initial load size = 3 * NETWORK_PAGE_SIZE
                        // ensure we're not requesting duplicating items, at the 2nd request
                        page + (params.loadSize / NETWORK_PAGE_SIZE)
                    }
                    Timber.tag("Photos PagingSource")
                        .e("nextKey init: $nextKey")
                    LoadResult.Page(
                        data = photoResponse.toCleanPhotos(),
                        prevKey = if (page == FLICKR_STARTING_PAGE_INDEX) null else page.minus(1),
                        nextKey = nextKey
                    )
                }
            } else {
                if (response.body() != null) {
                    if (response.body()!!.status == "fail") {
                        errorMessage =
                            response.body()!!.message ?: "An error occurred please try again later"
                    }
                }
                val throwable = GenericException(message = errorMessage)
                LoadResult.Error(throwable)
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
