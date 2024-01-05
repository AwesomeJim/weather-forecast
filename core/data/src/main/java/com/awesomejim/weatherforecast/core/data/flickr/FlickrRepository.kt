package com.awesomejim.weatherforecast.core.data.flickr

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.network.NETWORK_PAGE_SIZE
import com.awesomejim.weatherforecast.core.network.flickr.FlickrApiService
import javax.inject.Inject


class FlickrRepository @Inject constructor(
    private val flickrApiService: FlickrApiService
) {
    fun getPhotos(defaultLocation: DefaultLocation) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            prefetchDistance = 10,
            initialLoadSize = NETWORK_PAGE_SIZE * 2
        ),
        pagingSourceFactory = {
            FlickrPagingSource(
                flickrApiService,
                defaultLocation = defaultLocation
            )
        }
    ).flow
}
