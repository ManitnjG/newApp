package com.monochrome.music.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.monochrome.music.R
import com.monochrome.music.databinding.ActivityMainBinding
import com.monochrome.music.model.Song
import com.monochrome.music.viewmodel.MusicViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MusicViewModel by viewModels()
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songAdapter = SongAdapter { song -> viewModel.playSong(song) }
        binding.rvSongs.apply { layoutManager = LinearLayoutManager(this@MainActivity); adapter = songAdapter }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { query?.let { viewModel.searchSongs(it) }; return true }
            override fun onQueryTextChange(newText: String?) = false
        })
        binding.miniPlayer.btnPlayPause.setOnClickListener { viewModel.togglePlayPause() }
        binding.miniPlayer.btnNext.setOnClickListener { viewModel.skipNext() }
        binding.miniPlayer.btnPrev.setOnClickListener { viewModel.skipPrevious() }
        viewModel.songs.observe(this) { songAdapter.submitList(it) }
        viewModel.isLoading.observe(this) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.currentSong.observe(this) { song ->
            binding.miniPlayer.root.visibility = if (song != null) View.VISIBLE else View.GONE
            song?.let { binding.miniPlayer.tvTitle.text = it.title; binding.miniPlayer.tvArtist.text = it.artist }
        }
        viewModel.isPlaying.observe(this) { binding.miniPlayer.btnPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play) }
    }
}
