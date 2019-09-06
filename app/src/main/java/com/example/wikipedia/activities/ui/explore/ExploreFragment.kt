package com.example.wikipedia.activities.ui.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wikipedia.R
import com.example.wikipedia.activities.SearchActivity
import com.example.wikipedia.activities.adapters.ArticleCardRecyclerAdapter
import com.example.wikipedia.activities.lifecycleUtility.WikiApplication
import com.example.wikipedia.activities.managers.WikiManager
import com.google.android.material.card.MaterialCardView
import kotlin.Exception

class ExploreFragment : Fragment() {

    private lateinit var exploreViewModel: ExploreViewModel

    private var wikiManager : WikiManager? = null
    private var searchCardView : MaterialCardView? = null
    private var exploreRecycler : RecyclerView? = null
    private var adapter : ArticleCardRecyclerAdapter = ArticleCardRecyclerAdapter()
    private var refresher : SwipeRefreshLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        exploreViewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_explore, container, false)

        searchCardView = root.findViewById(R.id.search_cardView)
        exploreRecycler = root.findViewById(R.id.explore_article_recycler)
        refresher = root.findViewById(R.id.refresher)

        searchCardView!!.setOnClickListener { view ->
            startActivity(Intent(context, SearchActivity::class.java))
        }
        exploreRecycler!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        exploreRecycler!!.adapter = adapter

        refresher?.setOnRefreshListener {
            getRandomarticles()
        }
        getRandomarticles()
        return root
    }

    private fun getRandomarticles(){
        refresher?.isRefreshing = true

        try {
            wikiManager?.getRandom(15) { result ->
                adapter.currentResults.clear()
                adapter.currentResults.addAll(result.query.pages)
                activity!!.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    refresher?.isRefreshing = false
                }

            }
        } catch (ex : Exception){
            Log.d("llama", ex.message!!)
            //show alert
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage(ex.message).setTitle("Oops")
            val dialog = builder.create()
            dialog.show()
        }

    }
}