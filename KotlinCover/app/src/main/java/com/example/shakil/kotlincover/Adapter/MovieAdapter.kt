package com.example.shakil.kotlincover.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.shakil.kotlincover.Model.Movie
import com.example.shakil.kotlincover.R
import com.squareup.picasso.Picasso

class MovieAdapter(private val context: Context,
                   private val movieList:List<Movie>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView: View? = null
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.layout_item, null)

            val name = rowView!!.findViewById<View>(R.id.name) as TextView
            val imageView = rowView.findViewById<View>(R.id.image) as ImageView

            //Set Data
            Picasso.get().load(movieList[position].image).into(imageView)
            name.text = movieList[position].name
        }

        return rowView
    }

    override fun getItem(position: Int): Any {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return movieList.size
    }
}