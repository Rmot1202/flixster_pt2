package com.codepath.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.flixster.R.id

/**
 * [RecyclerView.Adapter] that can display a [NationalPark] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class flixsterRecyclerViewAdapter(
    private val parks: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<flixsterRecyclerViewAdapter.ParkViewHolder>() {

    // Inflate the item layout from XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_flixster, parent, false)
        return ParkViewHolder(view)
    }

    // ViewHolder class holds references to all UI elements inside the list item layout
    inner class ParkViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null

        // TODO: Step 4a - Add references for remaining views from XML


        val mParkImage: ImageView = mView.findViewById(R.id.ivPoster)
        val mParkName: TextView = mView.findViewById(id.tvTitle) as TextView
        val mParkDescription: TextView = mView.findViewById(id.tvOverview) as TextView

        override fun toString(): String {
            return mParkName.toString() + " '" + mParkDescription.text + "'"
        }
    }

    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
        val park = parks[position]

        // TODO: Step 4b - Bind the park data to the views
        holder.mParkName.text = park.name
        holder.mParkDescription.text = park.description

        // TODO: Step 4c - Use Glide to load the first image
        val imageUrl = park.imageUrl
        val fullUrl = "https://image.tmdb.org/t/p/w500/$imageUrl"
        Glide.with(holder.mView)
            .load(fullUrl)
            .centerInside()
            .into(holder.mParkImage)


        // Sets up click listener for this park item
        holder.mView.setOnClickListener {
            holder.mItem?.let { park ->
                mListener?.onItemClick(park)
            }
        }
    }

    // Tells the RecyclerView how many items to display
    override fun getItemCount(): Int {
        return parks.size
    }
}
