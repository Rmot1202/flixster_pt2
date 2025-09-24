package com.codepath.flixster

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"
const val EXTRA_MOVIE = "EXTRA_MOVIE"
const val EXTRA_AIRING_TODAY = "EXTRA_AIRING_TODAY"

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)

        val ivPoster: ImageView = findViewById(R.id.ivPoster)
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvMeta: TextView = findViewById(R.id.tvDetailMeta)
        val tvOverview: TextView = findViewById(R.id.tvOverview)

        val item = intent.getSerializableExtra(EXTRA_AIRING_TODAY) as? AiringTodayDetail
        if (item != null) {
            tvTitle.text = item.title ?: "Untitled"
            val rating = item.rating?.let { "⭐ %.1f".format(it) }
            val date = item.releaseDate
            val votes = item.voteCount?.let { "$it votes" }
            tvMeta.text = listOfNotNull(rating, date, votes).joinToString(" • ")

            tvOverview.text = item.overview ?: "No overview available."

            Glide.with(this).load(item.backdropImageUrl ?: item.posterImageUrl)
                .centerCrop().into(ivPoster)
        }
    }
    companion object { const val EXTRA_AIRING_TODAY = "EXTRA_AIRING_TODAY" }
}