package com.example.shakil.androidcover;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.shakil.androidcover.Adapter.MovieAdapter;
import com.example.shakil.androidcover.Common.Common;
import com.example.shakil.androidcover.Interface.IFirebaseLoadDone;
import com.example.shakil.androidcover.Model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    IFirebaseLoadDone iFirebaseLoadDone;
    DatabaseReference dbRef;

    AlertDialog dialog;

    //View
    FeatureCoverFlow coverFlow;
    TextSwitcher title;

    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        iFirebaseLoadDone = this;
        loadData();
    }

    private void loadData() {
        //Create dialog
        dialog = new SpotsDialog.Builder().setContext(MainActivity.this)
                .setMessage("Please wait...")
                .setCancelable(false)
                .build();
        dialog.show();

        dbRef = FirebaseDatabase.getInstance().getReference("Movies");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Movie> movies = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot movieSnapShot : dataSnapshot.getChildren()){
                    Movie movie = movieSnapShot.getValue(Movie.class);
                    movies.add(movie);
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(movies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<Movie> movieList) {
        dialog.dismiss();
        setupUI();
        Common.movieLoaded = movieList;

        movieAdapter = new MovieAdapter(this, movieList);
        coverFlow.setAdapter(movieAdapter);
        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                title.setText(Common.movieLoaded.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movie_index", position);
                startActivity(intent);
            }
        });
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        coverFlow = findViewById(R.id.coverFlow);
        title = findViewById(R.id.title);
        title.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                TextView txt = (TextView) inflater.inflate(R.layout.layout_title, null);
                return txt;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        title.setInAnimation(in);
        title.setOutAnimation(out);

        //Calculate screen size
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels/density;
        float dpWidth = outMetrics.widthPixels/density;

        coverFlow.setCoverHeight((int) dpHeight);
        coverFlow.setCoverWidth((int) dpWidth);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
