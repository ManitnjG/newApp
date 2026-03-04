package com.monochrome.music.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.monochrome.music.model.Song
import com.monochrome.music.network.ApiClient
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    val player: ExoPlayer = ExoPlayer.Builder(application).build()
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs
    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> = _currentSong
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) { _isPlaying.postValue(playing) }
        })
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.instance.getSongs()
                if (response.success) _songs.value = response.data
            } catch (e: Exception) {
                _error.value = "Failed to load: ${e.message}"
            } finally { _isLoading.value = false }
        }
    }

    fun searchSongs(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.instance.searchSongs(query)
                if (response.success) _songs.value = response.data
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
            } finally { _isLoading.value = false }
        }
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        val item = MediaItem.Builder().setUri(song.streamUrl)
            .setMediaMetadata(MediaMetadata.Builder().setTitle(song.title).setArtist(song.artist).build()).build()
        player.setMediaItem(item); player.prepare(); player.play()
    }

    fun togglePlayPause() { if (player.isPlaying) player.pause() else player.play() }
    fun skipNext() { if (player.hasNextMediaItem()) player.seekToNextMediaItem() }
    fun skipPrevious() { if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem() }
    override fun onCleared() { player.release(); super.onCleared() }
}
