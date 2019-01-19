package com.example.mg156.assignment9

import java.io.Serializable


data class MovieData(
        var adult: Boolean?,
        var backdrop_path: String?,
        var genre_ids: List<Int?>?,
        var original_language: String?,
        var original_title: String?,
        var overview: String?,
        var popularity: Double?,
        var poster_path: String?,
        var release_date: String?,
        var selection: Boolean?,
        var title: String?,
        var video: Boolean?,
        var vote_average: Double?,
        var vote_count: Int?
) : Serializable {
    constructor () : this(false, "", null, "", "", "", 0.0, "",
            "", false, "", false, 0.0,0)
}