package com.example.shakil.androidcover.Interface;

import com.example.shakil.androidcover.Model.Movie;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Movie> movieList);
    void onFirebaseLoadFailed(String message);
}
