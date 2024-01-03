package com.awesomejim.weatherforecast.repository

import com.awesomejim.weatherforecast.core.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.core.data.WeatherRepository
import com.awesomejim.weatherforecast.core.data.source.remote.DefaultRemoteWeatherDataSource
import com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.core.network.ApiService
import com.awesomejim.weatherforecast.core.network.ErrorType
import com.awesomejim.weatherforecast.core.network.NetworkHelper
import com.awesomejim.weatherforecast.core.network.RetrialResult
import com.awesomejim.weatherforecast.core.network.WeatherItemResponse
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
    val mockOpenWeatherService = mockk<com.awesomejim.weatherforecast.core.network.ApiService>(relaxed = true)

    // 2. Mock Context and NetworkHelper
    @MockK
    val mockNetworkHelper = mockk<com.awesomejim.weatherforecast.core.network.NetworkHelper>()

    @Test
    fun `when we fetch location weather data successfully, a valid mapped result is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(returnValue = true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.success<com.awesomejim.weatherforecast.core.network.WeatherItemResponse>(
                fakeSuccessWeatherResponse
            )

            val weatherRepository = createWeatherRepository()

            val expectedResult = fakeSuccessMappedWeatherResponse

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Success::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Success).data.locationId)
                .isEqualTo(expectedResult.locationId)
        }

    @Test
    fun `when we fetch weather data & a server-error occurs, a server-error is emitted`() =
        runBlocking {
            // let mockNetworkHelper.isNetworkConnected() always return true
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<com.awesomejim.weatherforecast.core.network.WeatherItemResponse>(
                503,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.SERVER)
        }

    @Test
    fun `when fetching weather data & a client error occurs, a client error is emitted`() =
        runBlocking {
            // let mockNetworkHelper.isNetworkConnected() always return true
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<com.awesomejim.weatherforecast.core.network.WeatherItemResponse>(
                404,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.CLIENT)
        }

    @Test
    fun `when fetching weather data & a 401 error occurs, unauthorized error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<com.awesomejim.weatherforecast.core.network.WeatherItemResponse>(
                401,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.UNAUTHORIZED)
        }

    @Test
    fun `when fetching weather data & a generic error occurs, generic-error is emitted`() =
        runBlocking {
            every { mockNetworkHelper.isNetworkConnected() }.returns(true)
            coEvery {
                mockOpenWeatherService.fetchWeatherDataWithCoordinates(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Response.error<com.awesomejim.weatherforecast.core.network.WeatherItemResponse>(
                800,
                "{}".toResponseBody()
            )

            val weatherRepository = createWeatherRepository()

            val actualResults = weatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.GENERIC)
        }

    @Test
    fun `when  fetching data & an IOException is thrown, a connection error is emitted`() =
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
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )

            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.IO_CONNECTION)
        }

    @Test
    fun `when fetching data & an unknown Exception is thrown,a generic error is emitted`() =
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
                defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = -122.084,
                    latitude = 37.4234
                ),
                units = "metric"
            )
            Truth.assertThat(actualResults).isInstanceOf(com.awesomejim.weatherforecast.core.network.RetrialResult.Error::class.java)
            Truth.assertThat((actualResults as com.awesomejim.weatherforecast.core.network.RetrialResult.Error).errorType)
                .isEqualTo(com.awesomejim.weatherforecast.core.network.ErrorType.GENERIC)
        }

    private fun createWeatherRepository(
        networkHelper: com.awesomejim.weatherforecast.core.network.NetworkHelper = mockNetworkHelper,
        remoteWeatherDataSource: com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource = com.awesomejim.weatherforecast.core.data.source.remote.DefaultRemoteWeatherDataSource(
            apiService = mockOpenWeatherService
        )
    ): com.awesomejim.weatherforecast.core.data.WeatherRepository =
        com.awesomejim.weatherforecast.core.data.DefaultWeatherRepository(
            remoteDataSource = remoteWeatherDataSource,
            networkHelper = networkHelper
        )
}
