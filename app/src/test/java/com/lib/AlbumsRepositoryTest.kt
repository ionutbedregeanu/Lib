package com.lib

import com.lib.cache.dao.AlbumDao
import com.lib.cache.model.CacheAlbum
import com.lib.data.SharedPrefsHelper
import com.lib.data.mapper.mapRemoteAlbumsToCache
import com.lib.data.repository.AlbumsRepository
import com.lib.remote.models.RemoteAlbum
import com.lib.remote.services.AlbumService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AlbumsRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var albumService: AlbumService

    @MockK(relaxed = true)
    private lateinit var albumDao: AlbumDao

    @MockK(relaxed = true)
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    private lateinit var albumsRepository: AlbumsRepository

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val remoteAlbum = RemoteAlbum(id = 1, userId = 1, title = "Remote Test")
    private val cacheAlbum = CacheAlbum(id = 1, userId = 1, title = "Cache Test")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        albumsRepository = AlbumsRepository(albumService, albumDao, sharedPrefsHelper)
    }

    @Test
    fun `given populate with data request, when swipe to refresh was made, then insert method is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { albumService.getAlbums() } returns Response.success(listOf(remoteAlbum))
            // When
            albumsRepository.populateLocalStorage(true)
            // Then
            coVerify(exactly = 1) { albumDao.insert(listOf(remoteAlbum).mapRemoteAlbumsToCache()) }
        }
    }

    @Test
    fun `given populate with data request, when swipe to refresh was made, then service method is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { albumService.getAlbums() } returns Response.success(listOf(remoteAlbum))
            // When
            albumsRepository.populateLocalStorage(true)
            // Then
            coVerify(exactly = 1) { albumService.getAlbums() }
        }
    }

    @Test
    fun `given albums request, when no swipe to refresh was made, then dao method is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { albumDao.getAlbums() } returns flowOf(listOf(cacheAlbum))
            // When
            albumsRepository.getAlbumsFromDatabase()
            // Then
            coVerify(exactly = 1) { albumDao.getAlbums() }
        }
    }

    @Test
    fun `given refresh, when the request is finished, then shared preferences helper method is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { albumService.getAlbums() } returns Response.success(listOf(remoteAlbum))
            coEvery { sharedPrefsHelper.getFirstRequestValue() } returns true

            // When
            albumsRepository.populateLocalStorage(false)
            // Then
            coVerify(exactly = 1) { sharedPrefsHelper.setFirstRequestValue(false) }
        }
    }

    @Test
    fun `given refresh, when is not a force refresh, then false is returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { sharedPrefsHelper.getFirstRequestValue() } returns false

            // When

            // Then
            assert(!albumsRepository.populateLocalStorage(false))
        }
    }

    @Test
    fun `given refresh, when is a force refresh, then true is returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // Given
            coEvery { albumService.getAlbums() } returns Response.success(listOf(remoteAlbum))
            coEvery { sharedPrefsHelper.getFirstRequestValue() } returns false
            // When

            // Then
            assert(albumsRepository.populateLocalStorage(true))
        }
    }
}
