package com.example.mg156.assignment9

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecyclerViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recycleView: RecyclerView
    lateinit var recycleViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        recycleView = rootview.findViewById(R.id.recyclerViewId) as RecyclerView
        recycleViewAdapter = RecyclerViewAdapter(context!!)
        recycleView.setAdapter(recycleViewAdapter)
        recycleView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        recycleView.setLayoutManager(mLayoutManager)

        recycleView.itemAnimator?.addDuration = 2000L
        recycleView.itemAnimator?.removeDuration = 3000L
        recycleView.itemAnimator?.changeDuration = 1000L
        recycleView.itemAnimator?.moveDuration = 1000L

        recycleViewAdapter.setMyItemClickListener(object : RecyclerViewAdapter.MyItemClickListener {

            override fun onOverFlowMenuClick(view: View, position: Int) {
                val popup = PopupMenu(activity!!, view)
                popup.setOnMenuItemClickListener { item ->
                    val id = item.itemId
                    when (id) {
                        R.id.contextual_or_pop_menu_copy -> {
                            recycleViewAdapter.addMovie(position)
                            recycleViewAdapter.notifyItemInserted(position)
                            true
                        }
                        R.id.contextual_or_pop_menu_delete -> {
                            recycleViewAdapter.removeMovie(position)
                            recycleViewAdapter.notifyItemRemoved(position)
                            true
                        }
                        else -> false
                    }
                }
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.contextual_or_popmenu, popup.menu)
                popup.show()
            }
        })

        return rootview
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RecyclerViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
