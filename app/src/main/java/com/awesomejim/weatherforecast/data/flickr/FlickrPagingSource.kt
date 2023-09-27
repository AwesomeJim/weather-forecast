package com.awesomejim.weatherforecast.data.flickr
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.awesomejim.weatherforecast.BuildConfig
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.flickr.FlickerPhotoResponse
import com.awesomejim.weatherforecast.di.flickr.FlickrApiService
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber

class FlickrPagingSource(
    private val newsApiService: FlickrApiService,
    private val defaultLocation: DefaultLocation
) : PagingSource<Int, FlickerPhotoResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FlickerPhotoResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlickerPhotoResponse> {
        return try {
            val page = params.key ?: 1
            Timber.tag("Photos PagingSource").e("load init: $page $defaultLocation")
            val response = newsApiService.getLocationPhotos(
                page = page,
                api_key = BuildConfig.FLICKR_API_KEY,
                lat = defaultLocation.latitude,
                lon = defaultLocation.longitude
            )
            if (response.isSuccessful && response.body() != null) {
                Timber.tag("Photos PagingSource").e("response init: ${response.body()!!.results.photos}")
                LoadResult.Page(
                    data = response.body()!!.results.photos,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (response.body()!!.results.photos.isEmpty()) null else page.plus(1),
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (response.body()!!.results.photos.isEmpty()) null else page.plus(1),
                )
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}