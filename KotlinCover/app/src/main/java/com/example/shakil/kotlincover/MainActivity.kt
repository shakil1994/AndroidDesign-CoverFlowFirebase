package com.example.shakil.kotlincover

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import com.example.shakil.kotlincover.Adapter.MovieAdapter
import com.example.shakil.kotlincover.Common.Common
import com.example.shakil.kotlincover.Interface.IFirebaseLoadDone
import com.example.shakil.kotlincover.Model.Movie
import com.google.firebase.database.*
import dmax.dialog.SpotsDialog
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), IFirebaseLoadDone {

    lateinit var iFirebaseLoadDone: IFirebaseLoadDone
    lateinit var dbRef:DatabaseReference

    lateinit var dialog: AlertDialog
    lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        iFirebaseLoadDone = this
        loadData()
    }

    private fun loadData() {
        //Create dialog
        dialog = SpotsDialog.Builder().setContext(this@MainActivity)
            .setMessage("Please wait...")
            .setCancelable(false)
            .build()
        dialog.show()

        dbRef = FirebaseDatabase.getInstance().getReference("Movies")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            var movies: MutableList<Movie> = ArrayList()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (movieSnapShot in dataSnapshot.children) {
                    val movie = movieSnapShot.getValue(Movie::class.java)
                    movies.add(movie!!)
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(movies)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.message)
            }
        })
    }

    override fun onFirebaseLoadSuccess(movieList: List<Movie>) {
        dialog.dismiss()
        setupUI()
        Common.movieLoaded = movieList

        adapter = MovieAdapter(this, movieList)
        cover_flow.adapter = adapter
        cover_flow.setOnScrollPositionListener(object : FeatureCoverFlow.OnScrollPositionListener {
            override fun onScrolledToPosition(position: Int) {
                textSwitcher.setText(Common.movieLoaded[position].name)
            }

            override fun onScrolling() {

            }
        })
        cover_flow.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("movie_index", position)
            startActivity(intent)
        }
    }

    private fun setupUI() {
        setContentView(R.layout.activity_main)

        textSwitcher.setFactory {
            val inflater = LayoutInflater.from(this@MainActivity)
            inflater.inflate(R.layout.layout_title, null) as TextView
        }

        val `in` = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        textSwitcher.inAnimation = `in`
        textSwitcher.outAnimation = out

        //Calculate screen size
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density

        cover_flow.coverHeight = dpHeight.toInt()
        cover_flow.coverWidth = dpWidth.toInt()
    }

    override fun onFirebaseLoadFailed(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
