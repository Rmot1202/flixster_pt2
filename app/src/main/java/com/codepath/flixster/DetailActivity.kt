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

    private lateinit var ivPoster: ImageView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailMeta: TextView
    private lateinit var tvOverview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the layout you pasted (save it as res/layout/detail.xml)
        setContentView(R.layout.detail)

        ivPoster = findViewById(R.id.ivPoster)
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailMeta = findViewById(R.id.tvDetailMeta)
        tvOverview = findViewById(R.id.tvOverview)

        // Try TV detail first, then Movie
        val tv = intent.getSerializableExtra(EXTRA_AIRING_TODAY) as? AiringTodayDetail
        val movie = intent.getSerializableExtra(EXTRA_MOVIE) as? Movie

        when {
            tv != null -> bindAiringToday(tv)
            movie != null -> bindMovie(movie)
            else -> {
                tvDetailTitle.text = "No item provided"
                tvDetailMeta.text = ""
                tvOverview.text = ""
            }
        }
    }

    private fun bindAiringToday(item: AiringTodayDetail) {
        tvDetailTitle.text = item.title ?: "Untitled"

        val rating = item.rating?.let { "⭐ ${"%.1f".format(it)}" }
        val date = item.releaseDate
        val votes = item.votecount?.let { "$it votes" }
        tvDetailMeta.text = listOfNotNull(rating, date, votes).joinToString(" • ")

        tvOverview.text = item.overview ?: "No overview available."

        // Prefer backdrop; fall back to poster
        val heroUrl = item.backdropImageUrl ?: item.posterImageUrl
        Glide.with(this)
            .load(heroUrl)
            .centerCrop()
            .into(ivPoster)
    }

    private fun bindMovie(item: Movie) {
        tvDetailTitle.text = item.title ?: "Untitled"

        val rating = item.rating?.let { "⭐ ${"%.1f".format(it)}" } ?: "⭐ N/A"
        tvDetailMeta.text = rating

        // You said Movie only keeps title/rating/poster, so no overview here
        tvOverview.text = ""

        Glide.with(this)
            .load(item.posterImageUrl)
            .centerCrop()
            .into(ivPoster)
    }
}
