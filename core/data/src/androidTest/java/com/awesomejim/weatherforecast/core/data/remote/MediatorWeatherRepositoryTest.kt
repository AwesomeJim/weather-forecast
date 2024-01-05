package com.awesomejim.weatherforecast.core.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.awesomejim.weatherforecast.core.data.fake.FakeResponseData
import com.awesomejim.weatherforecast.core.data.source.local.MediatorRepository
import com.awesomejim.weatherforecast.core.data.source.local.MediatorWeatherRepository
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationItem
import com.awesomejim.weatherforecast.core.data.source.remote.DefaultRemoteWeatherDataSource
import com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.core.data.utils.RetrialResult
import com.awesomejim.weatherforecast.core.database.LocationDatabase
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import com.awesomejim.weatherforecast.core.network.ApiService
import com.awesomejim.weatherforecast.core.network.NetworkHelper
import com.awesomejim.weatherforecast.core.network.model.WeatherItemResponse
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class MediatorWeatherRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: LocationDatabase
    private lateinit var locationItemDao: LocationItemDao

    @Before
    fun setup() {
        hiltRule.inject()
        locationItemDao = database.locationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @MockK
    val mockOpenWeatherService = mockk<ApiService>(relaxed = true)

    @MockK
    val mockNetworkHelper = mockk<NetworkHelper>()

    @Test
    fun assert_when_we_fetch_location_weather_data_successfully_a_mapped_result_is_emitted() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(returnValue = true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.success<WeatherItemResponse>(
               FakeResponseData.fakeSuccessWeatherResponse
            )

            val weatherRepository = createWeatherRepository()

            val expectedResult = FakeResponseData.fakeSuccessMappedWeatherResponse

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric",
                locationId = 1695383
            )
            // val actualResults = expectMostRecentItem()
            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Success::class.java)
            Truth.assertThat((actualResults as RetrialResult.Success).data.locationId)
                .isEqualTo(expectedResult.locationId)
            val locationItemData = FakeResponseData.fakeSuccessMappedWeatherResponse

            // When the repository emits a value
            val actual = locationItemDao.getLocationById(locationItemData.locationId)
                ?.toLocationItem() // Returns the first item in the flow
            Truth.assertThat(locationItemData.locationId).isEqualTo(actual?.locationId)
        }

    private fun createWeatherRepository(
        networkHelper: NetworkHelper = mockNetworkHelper,
        remoteWeatherDataSource: RemoteDataSource = DefaultRemoteWeatherDataSource(
            apiService = mockOpenWeatherService
        ),
        dao: LocationItemDao = locationItemDao
    ): MediatorRepository =
        MediatorWeatherRepository(
            remoteDataSource = remoteWeatherDataSource,
            networkHelper = networkHelper,
            locationItemDao = dao

        )
}
