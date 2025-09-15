package com.codepath.flixster

import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single park from the National Parks API.
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */
class Movie {

    // Name field
    @JvmField
    @SerializedName("fullName")
    var name: String? = null

    // Description field
    @JvmField
    @SerializedName("description")
    var description: String? = null

    //TODO parkImageUrl
    @JvmField
    @SerializedName("images")
    var images: List<Image>? = null

    // Convenience property to access the first image’s URL
    val imageUrl: String? get() = images?.firstOrNull()?.url

    class Image {
        @SerializedName("url")
        var url: String? = null
    }

    //TODO-STRETCH-GOALS
}
