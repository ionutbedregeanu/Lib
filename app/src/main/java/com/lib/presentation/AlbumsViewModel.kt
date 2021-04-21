package com.lib.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.data.repository.AlbumsRepository
import com.lib.presentation.mapper.mapDataAlbumsToUI
import com.lib.ui.model.UiAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AlbumsViewModel @Inject constructor(private val albumsRepository: AlbumsRepository) : ViewModel() {
    val albums: MutableLiveData<List<UiAlbum>> = MutableLiveData()
    val displayError: MutableLiveData<Unit> = MutableLiveData()
    val shouldDisplayLoader: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getAlbums()
    }

    open fun getAlbums() {
        viewModelScope.launch {
            albumsRepository.getAlbumsFromDatabase()
                .onStart {
                    refresh(false)
                }
                .catch {
                    displayError.postValue(Unit)
                }
                .collect { response ->
                    albums.postValue(response.mapDataAlbumsToUI())
                }
        }
    }

    fun refresh(isForcedRefresh: Boolean = true) {
        viewModelScope.launch {
            shouldDisplayLoader.postValue(true)
            try {
                albumsRepository.populateLocalStorage(isForcedRefresh)
            } catch (exception: Throwable) {
                displayError.postValue(Unit)
            }
            shouldDisplayLoader.postValue(false)
        }
    }
}
