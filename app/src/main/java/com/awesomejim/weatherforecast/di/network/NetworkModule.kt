package com.awesomejim.weatherforecast.di.network


import com.awesomejim.weatherforecast.BuildConfig
import com.awesomejim.weatherforecast.di.ApiService
import com.awesomejim.weatherforecast.di.flickr.FlickrApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    fun provideBaseUrl(): Pair<String, String> =
        Pair(BuildConfig.OPEN_WEATHER_BASE_URL, BuildConfig.FLICKR_PHOTOS_BASE_URL)

    @Singleton
    @Provides
    fun provideConnectionSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
        return HttpLoggingInterceptor().also {
            it.level = level
        }
    }

    /**
     *
     * @param spec ConnectionSpec
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(spec: ConnectionSpec, loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionSpecs(Collections.singletonList(spec))
            .addInterceptor(loggingInterceptor)
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseURL: Pair<String, String>,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseURL.first)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideFlickrApiService(
        okHttpClient: OkHttpClient,
        baseURL: Pair<String, String>,
        json: Json
    ): FlickrApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseURL.second)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build().create(FlickrApiService::class.java)
    }
}