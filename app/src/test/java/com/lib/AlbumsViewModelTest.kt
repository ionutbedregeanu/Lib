package com.lib

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lib.data.model.Album
import com.lib.data.repository.AlbumsRepository
import com.lib.presentation.AlbumsViewModel
import com.lib.presentation.mapper.mapDataAlbumsToUI
import com.lib.ui.model.UiAlbum
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var albumsRepository: AlbumsRepository

    @MockK(relaxed = true)
    lateinit var albumsObserver: Observer<List<UiAlbum>>

    @MockK(relaxed = true)
    lateinit var loaderObserver: Observer<Boolean>

    @MockK(relaxed = true)
    lateinit var errorObserver: Observer<Unit>

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var albumsViewModel: AlbumsViewModel
    private val album = Album(id = 1, userId = 1, title = "Test")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        albumsViewModel = AlbumsViewModel(albumsRepository)
    }

    @Test
    fun `given albums screen is opened, when getAlbumsFromDatabase is returned with success, then albums live data is populated`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //Given
            albumsViewModel.albums.observeForever(albumsObserver)
            coEvery { albumsRepository.getAlbumsFromDatabase() } returns flowOf(listOf(album))

            //When
            albumsViewModel.getAlbums()
            advanceTimeBy(3001)

            //Then
            verify { albumsObserver.onChanged(listOf(album).mapDataAlbumsToUI()) }
        }
    }

    @Test
    fun `given main screen is opened, when refresh is called, then loader live data is populated`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //Given
            albumsViewModel.shouldDisplayLoader.observeForever(loaderObserver)
            coEvery { albumsRepository.populateLocalStorage() } returns false

            //When
            albumsViewModel.refresh()

            //Then
            verifyOrder {
                loaderObserver.onChanged(true)
                loaderObserver.onChanged(false)
            }
        }
    }

    @Test
    fun `given albums screen is opened, when request failed, then an error is displayed`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //Given
            albumsViewModel.displayError.observeForever(errorObserver)
            coEvery { albumsRepository.populateLocalStorage(true) } throws Throwable()

            //When
            albumsViewModel.refresh()

            //Then
            verify { errorObserver.onChanged(Unit) }
        }
    }
}
