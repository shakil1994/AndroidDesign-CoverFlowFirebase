package com.example.shakil.kotlincover

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.shakil.kotlincover.Common.Common
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //GET Movie index
        if (intent != null) {
            val movie_index = intent.getIntExtra("movie_index", -1)
            if (movie_index != -1) {
                loadMovieIndex(movie_index)
            }
        }
    }

    private fun loadMovieIndex(index: Int) {
        val movie = Common.movieLoaded[index]

        Picasso.get().load(movie.image).into(movie_image)
        movie_title.text = movie.name
        movie_description.text = movie.description
    }
}
