package com.lib

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lib.cache.dao.AlbumDao
import com.lib.cache.model.CacheAlbum
import com.lib.di.AlbumDaoModule
import com.lib.ui.AlbumsActivity
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AlbumDaoModule::class)
@RunWith(AndroidJUnit4::class)
open class AlbumsActivityTest {

    var albumDao = mock<AlbumDao>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val album = CacheAlbum(id = 1, userId = 1, title = "Test")

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    inner class MockRepository {
        @Provides
        fun provideAlbumDao(): AlbumDao = albumDao
    }

    @Test
    fun checkIfAlbumsRecyclerContainsElements() {
        runBlocking {
            whenever(albumDao.getAlbums()).thenReturn(flowOf(listOf(album)))
            launchActivity<AlbumsActivity>()
            onView(withId(R.id.albums_container)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }
    }

    @Test
    fun checkIfEmptyListViewIsDisplayed() {
        runBlocking {
            whenever(albumDao.getAlbums()).thenReturn(flowOf(listOf()))
            launchActivity<AlbumsActivity>()
            onView(withId(R.id.empty_list_message)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkIfEmptyListViewIsHidden() {
        runBlocking {
            whenever(albumDao.getAlbums()).thenReturn(flowOf(listOf(album)))
            launchActivity<AlbumsActivity>()
            onView(withId(R.id.empty_list_message)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun checkIfSwipeToRefreshContainerIsDisplayed() {
        runBlocking {
            whenever(albumDao.getAlbums()).thenReturn(flowOf(listOf(album)))
            launchActivity<AlbumsActivity>()
            onView(withId(R.id.albums_swipe_container)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
        }
    }

    open fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }
}
