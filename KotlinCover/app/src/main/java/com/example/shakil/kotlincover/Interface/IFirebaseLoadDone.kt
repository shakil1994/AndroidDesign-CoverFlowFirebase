package com.example.shakil.kotlincover.Interface

import com.example.shakil.kotlincover.Model.Movie

interface IFirebaseLoadDone {
    fun onFirebaseLoadSuccess(movieList: List<Movie>)
    fun onFirebaseLoadFailed(message: String)
}