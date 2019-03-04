package com.example.shakil.androidcover;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shakil.androidcover.Common.Common;
import com.example.shakil.androidcover.Model.Movie;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView title, description;
    KenBurnsView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.movie_title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.movie_image);

        //GET Movie index
        if (getIntent() != null){
            int movie_index = getIntent().getIntExtra("movie_index", -1);
            if (movie_index != -1){
                loadMovieIndex(movie_index);
            }
        }
    }

    private void loadMovieIndex(int index) {
        Movie movie = Common.movieLoaded.get(index);

        Picasso.get().load(movie.getImage()).into(image);
        title.setText(movie.getName());
        description.setText(movie.getDescription());
    }
}
