package com.example.mg156.assignment9

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar

class MovieActivity : AppCompatActivity() {

    lateinit var recFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Movie Details"


        if (savedInstanceState == null) {
            recFragment = RecyclerViewFragment.newInstance(R.id.recyclerViewId.toString(),"")
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                recFragment).commit()
    }
}
