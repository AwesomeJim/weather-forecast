package com.awesomejim.weatherforecast.data.flickr
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.awesomejim.weatherforecast.BuildConfig
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.flickr.FlickerPhotoResponse
import com.awesomejim.weatherforecast.di.flickr.FlickrApiService
import com.awesomejim.weatherforecast.utilities.GenericException
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber

class FlickrPagingSource(
    private val newsApiService: FlickrApiService,
    private val defaultLocation: DefaultLocation
) : PagingSource<Int, FlickerPhotoResponse>() {

    var errorMessage: String = "An error occurred please try again later"

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
                if (response.body()!!.status == "fail") {
                    errorMessage =
                        response.body()!!.message ?: "An error occurred please try again later"

                    val throwable = GenericException(message = errorMessage)
                    LoadResult.Error(throwable)
                } else {
                    Timber.tag("Photos PagingSource")
                        .e("response init: ${response.body()!!.results?.photos}")
                    val photoResponse = response.body()!!.results?.photos ?: emptyList()
                    LoadResult.Page(
                        data = photoResponse,
                        prevKey = if (page == 1) null else page.minus(1),
                        nextKey = if (photoResponse.isEmpty()) null else page.plus(1),
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
