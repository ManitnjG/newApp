package com.monochrome.music.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monochrome.music.R
import com.monochrome.music.databinding.ItemSongBinding
import com.monochrome.music.model.Song
import java.util.concurrent.TimeUnit

class SongAdapter(private val onSongClick: (Song) -> Unit) : ListAdapter<Song, SongAdapter.SongViewHolder>(DiffCallback()) {
    inner class SongViewHolder(private val b: ItemSongBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(song: Song) {
            b.tvTitle.text = song.title
            b.tvArtist.text = song.artist
            b.tvDuration.text = "%d:%02d".format(TimeUnit.MILLISECONDS.toMinutes(song.duration), TimeUnit.MILLISECONDS.toSeconds(song.duration) % 60)
            Glide.with(b.root).load(song.artworkUrl).placeholder(R.drawable.ic_music_note).centerCrop().into(b.ivArtwork)
            b.root.setOnClickListener { onSongClick(song) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SongViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) = holder.bind(getItem(position))
    class DiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(a: Song, b: Song) = a.id == b.id
        override fun areContentsTheSame(a: Song, b: Song) = a == b
    }
}
