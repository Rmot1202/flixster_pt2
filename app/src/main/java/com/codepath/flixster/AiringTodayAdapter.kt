package com.codepath.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AiringTodayAdapter(
    private var shows: List<AiringTodayDetail>,
    private val onClick: (AiringTodayDetail) -> Unit
) : RecyclerView.Adapter<AiringTodayAdapter.ShowVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_flixster, parent, false) // ✅ row item
        return ShowVH(v)
    }

    inner class ShowVH(v: View) : RecyclerView.ViewHolder(v) {
        val ivPoster: ImageView = v.findViewById(R.id.ivPoster)
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvRating: TextView = v.findViewById(R.id.tvRating)

        init {
            v.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(shows[pos])
            }
        }
    }

    override fun onBindViewHolder(h: ShowVH, position: Int) {
        val item = shows[position]
        h.tvTitle.text = item.title ?: "Untitled"
        h.tvRating.text = "⭐ " + (item.rating?.let { String.format("%.1f", it) } ?: "N/A")

        val radius = h.itemView.resources.getDimensionPixelSize(R.dimen.poster_corner_radius)

        Glide.with(h.itemView)
            .load(item.posterImageUrl)
            .transform(CenterCrop(), RoundedCorners(radius)) // 👈 rounded corners
            .into(h.ivPoster)
    }

    override fun getItemCount() = shows.size

    fun submitList(newList: List<AiringTodayDetail>) {
        shows = newList
        notifyDataSetChanged()
    }
}
