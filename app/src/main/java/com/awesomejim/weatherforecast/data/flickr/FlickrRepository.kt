package com.awesomejim.weatherforecast.data.flickr

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.flickr.FlickrApiService
import javax.inject.Inject

const val PAGE_SIZE = 20
class FlickrRepository @Inject constructor(
    private val flickrApiService: FlickrApiService
) {
    fun getPhotos(defaultLocation: DefaultLocation) = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 10,
            initialLoadSize = PAGE_SIZE
        ),
        pagingSourceFactory = {
            FlickrPagingSource(
                flickrApiService,
                defaultLocation = defaultLocation
            )
        }
    ).flow
}