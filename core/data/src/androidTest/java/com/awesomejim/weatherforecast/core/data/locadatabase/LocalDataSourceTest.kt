package com.awesomejim.weatherforecast.core.data.locadatabase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.awesomejim.weatherforecast.core.data.fake.FakeLocalDataSource
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationEntity
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationItem
import com.awesomejim.weatherforecast.core.database.LocationDatabase
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class LocalDataSourceTest {

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

    @Test
    fun insert_Insert_Location_Is_Successful_And_Available_InFlow() = runBlocking {
        val locationItemData = FakeLocalDataSource.locationList.first()
        locationItemDao.insertLocation(locationItemData.toLocationEntity())
        // When the repository emits a value
        // Returns the first item in the flow
        val actual = locationItemDao.getLocationById(locationItemData.locationId)?.toLocationItem(false)

        assertThat(locationItemData).isEqualTo(actual)
    }

    @Test
    fun insert_loading_all_location_items_is_Successful_And_Available_InFlow() = runBlocking {
        val locationItemDataList = FakeLocalDataSource.locationList
        // Insert the 3 fake items into the list
        locationItemDataList.forEach { item ->
            locationItemDao.insertLocation(item.toLocationEntity())
        }
        // When the repository emits a value
        val actual = locationItemDao.loadAllLocation().first()?.map {
            it.toLocationItem()
        }
        assertThat(locationItemDataList.first()).isIn(actual)
        assertThat(locationItemDataList).containsAnyIn(actual)
    }

    @Test
    fun assert_DeletedItem_ShouldNot_be_present_inFlow(): Unit = runBlocking {
        val locationItemDataList = FakeLocalDataSource.locationList
        // Insert the 3 fake items into the list
        locationItemDataList.forEach { item ->
            locationItemDao.insertLocation(item.toLocationEntity())
        }
        // we delete the 2nd item from db
        locationItemDao.deleteLocation(locationItemDataList[1].toLocationEntity())

        // our db should have only 2 items
        val list = locationItemDao.loadAllLocation().first()
        assertThat(list?.size).isEqualTo(2)
        // deleted item should not exits in the db
        val actual = locationItemDao
            .getLocationById(locationItemDataList[1].locationId)
            ?.toLocationItem(false)
        assertThat(actual).isNull()
    }

    @Test
    fun assert_getLocationById_of_non_existing_item_Should_Not_not_return_item_inFlow(): Unit =
        runBlocking {
            // try to load a dummy location using a dummy id
            val actual = locationItemDao.getLocationById(637733)?.toLocationItem()
            assertThat(actual).isNull()
        }

    @Test
    fun assert_clear_All_Items_Should_Not_not_return_items_inFlow() = runBlocking {
        val locationItemDataList = FakeLocalDataSource.locationList
        // Insert the 3 fake items into the list
        locationItemDataList.forEach { item ->
            locationItemDao.insertLocation(item.toLocationEntity())
        }
        // delete all items
        locationItemDao.clearAll()

        // deleted item should not exits in the db
        val actual = locationItemDao
            .getLocationById(locationItemDataList[1].locationId)
            ?.toLocationItem()
        assertThat(actual).isNull()
    }
}
