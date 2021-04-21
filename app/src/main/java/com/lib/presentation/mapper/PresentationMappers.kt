package com.lib.presentation.mapper

import com.lib.data.model.Album
import com.lib.ui.model.UiAlbum

fun List<Album>.mapDataAlbumsToUI() = map { album ->
    UiAlbum(
        id = album.id,
        userId = album.userId,
        title = album.title
    )
}
