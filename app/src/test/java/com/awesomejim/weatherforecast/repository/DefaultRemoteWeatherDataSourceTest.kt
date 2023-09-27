package com.awesomejim.weatherforecast.repository

import com.awesomejim.weatherforecast.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.data.WeatherRepository
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.source.remote.DefaultRemoteWeatherDataSource
import com.awesomejim.weatherforecast.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.di.ApiService
import com.awesomejim.weatherforecast.di.network.ErrorType
import com.awesomejim.weatherforecast.di.network.NetworkHelper
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.di.network.WeatherItemResponse
import com.awesomejim.weatherforecast.fake.FakeResponseData.fakeSuccessMappedWeatherResponse
import com.awesomejim.weatherforecast.fake.FakeResponseData.fakeSuccessWeatherResponse
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class DefaultRemoteWeatherDataSourceTest {

    @MockK
    val mockOpenWeatherService = mockk<ApiService>(relaxed = true)

    // 2. Mock Context and NetworkHelper
    @MockK
    val mockNetworkHelper = mockk<NetworkHelper>()

    @Test
    fun `when we fetch location weather data successfully, then a successfully mapped result is emitted`() =
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
                fakeSuccessWeatherResponse
            )

            val weatherRepository = createWeatherRepository()

            val expectedResult = fakeSuccessMappedWeatherResponse

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Success::class.java)
            Truth.assertThat((actualResults as RetrialResult.Success).data.locationId)
                .isEqualTo(expectedResult.locationId)
            
        }

    @Test
    fun `when we fetch weather data and a server error occurs, then a server error is emitted`() =
        runBlocking {
            //let mockNetworkHelper.isNetworkConnected() always return true
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<WeatherItemResponse>(
                503,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.SERVER)
        }

    @Test
    fun `when we fetch weather data and a client error occurs, then a client error is emitted`() =
        runBlocking {
            //let mockNetworkHelper.isNetworkConnected() always return true
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<WeatherItemResponse>(
                404,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.CLIENT)
        }


    @Test
    fun `when we fetch weather data and an unauthorized error occurs, then an unauthorized error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<WeatherItemResponse>(
                401,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.UNAUTHORIZED)
        }


    @Test
    fun `when we fetch weather data and a generic error occurs, then a generic error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<WeatherItemResponse>(
                800,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.GENERIC)
        }

    @Test
    fun `when we fetch weather data and an IOException is thrown, then a connection error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws IOException()

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.IO_CONNECTION)
        }

    @Test
    fun `when we fetch weather data and an unknown Exception is thrown, then a generic error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as RetrialResult.Error).errorType)
                .isEqualTo(ErrorType.GENERIC)
        }


    private fun createWeatherRepository(
        networkHelper: NetworkHelper = mockNetworkHelper,
        remoteWeatherDataSource: RemoteDataSource = DefaultRemoteWeatherDataSource(
            apiService = mockOpenWeatherService
        )
    ): WeatherRepository = DefaultWeatherRepository(
        remoteDataSource = remoteWeatherDataSource,
        networkHelper = networkHelper
    )
}