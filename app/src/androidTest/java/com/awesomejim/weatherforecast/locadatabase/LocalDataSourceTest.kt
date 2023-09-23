package com.awesomejim.weatherforecast.locadatabase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.awesomejim.weatherforecast.data.source.local.dao.LocationItemDao
import com.awesomejim.weatherforecast.data.source.local.db.LocationDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber
import java.util.Date
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
    fun insertUser() = runBlocking {
//        val journal = JournalEntry(
//            id = 1,
//            title = "Testing",
//            body = "Testing with Hilt",
//            updatedOn = Date()
//        )
//        jounalDao.insertJournal(journal)
//        // When the repository emits a value
//        val actual = jounalDao.loadAllJournals().first() // Returns the first item in the flow
//
//        assertThat(journal).isIn(actual)
        Timber.e("")
    }
}