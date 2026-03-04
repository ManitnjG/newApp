package com.monochrome.music.model

data class Song(val id: String, val title: String, val artist: String, val album: String, val duration: Long, val streamUrl: String, val artworkUrl: String?)
data class Playlist(val id: String, val name: String, val description: String?, val songs: List<Song>, val coverUrl: String?)
data class ApiResponse<T>(val data: T, val message: String?, val success: Boolean)
