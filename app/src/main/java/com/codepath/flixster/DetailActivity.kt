package com.codepath.flixster

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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

        // Safely read the Serializable extra on all Android versions
        val item: AiringTodayDetail? =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getSerializableExtra(EXTRA_AIRING_TODAY, AiringTodayDetail::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(EXTRA_AIRING_TODAY) as? AiringTodayDetail
            }

        if (item == null) {
            Toast.makeText(this, "No show data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bind UI
        tvTitle.text = item.title ?: "Untitled"
        val rating = item.rating?.let { "⭐ %.1f".format(it) }
        val date = item.releaseDate
        val votes = item.voteCount?.let { "$it votes" }
        tvMeta.text = listOfNotNull(rating, date, votes).joinToString(" • ")
        tvOverview.text = item.overview ?: "No overview available."

        // Rounded corners
        val radius = resources.getDimensionPixelSize(R.dimen.hero_corner_radius)
        val heroUrl = item.backdropImageUrl ?: item.posterImageUrl
        Glide.with(this)
            .load(heroUrl)
            .transform(CenterCrop(), RoundedCorners(radius))
            .into(ivPoster)
    }

    companion object {
        const val EXTRA_AIRING_TODAY = "EXTRA_AIRING_TODAY"
    }
}
