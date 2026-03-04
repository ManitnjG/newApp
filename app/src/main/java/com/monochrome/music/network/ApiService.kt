package com.monochrome.music.network

import com.monochrome.music.model.ApiResponse
import com.monochrome.music.model.Playlist
import com.monochrome.music.model.Song
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.monochromemusic.com/v1/"

interface MusicApiService {
    @GET("songs") suspend fun getSongs(@Query("page") page: Int = 1): ApiResponse<List<Song>>
    @GET("songs/search") suspend fun searchSongs(@Query("q") query: String): ApiResponse<List<Song>>
    @GET("playlists") suspend fun getPlaylists(): ApiResponse<List<Playlist>>
}

object ApiClient {
    val instance: MusicApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MusicApiService::class.java)
    }
}
