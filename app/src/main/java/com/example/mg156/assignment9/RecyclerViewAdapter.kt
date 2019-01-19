package com.example.mg156.assignment9

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder>() {

    var myListener: MyItemClickListener? = null
    var lastPosition = -1
    val movieList = ArrayList<MovieData>()
    val TAG = "FB ADAPTER"
    var mcontext = context

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mRef = mDatabase.child("movies")

    fun bindMovieData(){
        mRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot != null){
                    for(data in dataSnapshot.children){
                        movieList.add(data.getValue(MovieData::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
            }
        })
    }

    var childEventListener = object: ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(context, " Fail to load data ", Toast.LENGTH_SHORT).show()
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            val data = p0.getValue<MovieData>(MovieData::class.java)
            val key = p0.key
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            Log.d(TAG, " child event listener - onChildChanged " + p0.toString())
            val data = p0.getValue<MovieData>(MovieData::class.java)
            val key = p0.key

            data!!.title = key!!
            var index = 0
            for(movie in movieList){
                if(movie.title == p0.key){
                    break
                }
                index++
            }
            movieList.removeAt(index)
            notifyItemRemoved(index)
            movieList.add(index,data)
            notifyItemInserted(index)

        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            Log.d(TAG, " child event listener - onChildAdded " + p0.toString())
            val data = p0.getValue<MovieData>(MovieData::class.java)
            val key = p0.key

            data!!.title = key
            movieList.add(0,data!!)
            notifyItemInserted(0)

        }

        override fun onChildRemoved(p0: DataSnapshot) {
            Log.d(TAG, " child event listener - onChildRemoved " + p0.toString())
            val data = p0.getValue<MovieData>(MovieData::class.java)
            val key = p0.key

            var index = 0
            for(movie in movieList){
                if(movie.title == p0.key){
                    break
                }
                index++
            }
            movieList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun removeMovie(position: Int) {
        val movie = movieList[position]
        mRef.child(movie.title!!).removeValue();
    }

    fun addMovie(position: Int) {
        val movie = movieList[position].copy()
        movie.title = movie.title + "_New"
        mRef.child(movie.title!!).setValue(movie).addOnSuccessListener {
            Toast.makeText(mcontext,"Movie Added",Toast.LENGTH_LONG).show()
        }
    }

    interface MyItemClickListener {
        fun onOverFlowMenuClick(view: View, position: Int)
    }

    fun setMyItemClickListener(listener: MyItemClickListener) {
        this.myListener = listener
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val v: View
        v = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MovieViewHolder(v)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        val url = "https://image.tmdb.org/t/p/w780" + movie.poster_path
        Picasso.get().load(url).fit().into(holder.moviePoster)
        holder.movieGenre.setText("Rating: " + movie?.vote_average.toString())
        holder.movieTitle.text = movie.title
        holder.movieOverview.text = movie.overview

        setAnimation(holder.itemView,position)
    }


    fun setAnimation(view: View, position: Int) {
        if (position != lastPosition) {
            var animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            view.startAnimation(animation);
            lastPosition = position
        }
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moviePoster = view.findViewById<ImageView>(R.id.item_image)
        val movieTitle = view.findViewById<TextView>(R.id.item_title)
        val movieOverview = view.findViewById<TextView>(R.id.item_overview)
        val movieGenre = view.findViewById<TextView>(R.id.item_genre)
        val movie_overflow_image = view.findViewById<ImageView>(R.id.item_overflow_image)

        init {
            movie_overflow_image.setOnClickListener(View.OnClickListener { v ->
                myListener!!.onOverFlowMenuClick(v, adapterPosition)
            })
        }
    }

    init {
        mcontext = context
        mRef.addChildEventListener(childEventListener)
    }
}