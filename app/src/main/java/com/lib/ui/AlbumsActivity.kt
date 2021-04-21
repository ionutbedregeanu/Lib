package com.lib.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.lib.R
import com.lib.presentation.AlbumsViewModel
import com.lib.ui.adapter.AlbumsAdapter
import com.lib.ui.adapter.ItemDecorator
import com.lib.ui.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_albums.main_container as mainContainer
import kotlinx.android.synthetic.main.activity_albums.empty_list_message as emptyListMessage
import kotlinx.android.synthetic.main.activity_albums.albums_swipe_container as swipeContainer
import kotlinx.android.synthetic.main.activity_albums.albums_container as albumsContainer

@AndroidEntryPoint
class AlbumsActivity : AppCompatActivity() {

    private val viewModel: AlbumsViewModel by viewModels()
    private val albumsAdapter = AlbumsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)
        initialize()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        swipeContainer.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initialize() {
        albumsContainer.apply {
            adapter = albumsAdapter
            addItemDecoration(ItemDecorator())
        }
    }

    private fun setObservers() {
        viewModel.albums.observe(this, { albums ->
            emptyListMessage.isVisible = albums.isEmpty()
            albumsAdapter.submitList(albums)
        })

        viewModel.shouldDisplayLoader.observe(this, { isVisible ->
            swipeContainer.isRefreshing = isVisible
        })

        viewModel.displayError.observe(this, {
            displaySnackBar(mainContainer, R.color.red, R.string.something_went_wrong)
        })
    }
}
