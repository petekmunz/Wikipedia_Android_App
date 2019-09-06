package com.example.wikipedia.activities.ui.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wikipedia.R
import com.example.wikipedia.activities.ArticleDetailActivity
import com.example.wikipedia.activities.adapters.ArticleCardRecyclerAdapter
import com.example.wikipedia.activities.adapters.ArticleListItemRecyclerAdapter
import com.example.wikipedia.activities.lifecycleUtility.WikiApplication
import com.example.wikipedia.activities.managers.WikiManager
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.doAsync

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private var favoriteRecycler : RecyclerView? = null
    private var wikiManager : WikiManager? = null
    private val adapter = ArticleCardRecyclerAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        favoriteRecycler = root.findViewById(R.id.favorite_article_recycler)

        favoriteRecycler!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        favoriteRecycler!!.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val favoritesArticles = wikiManager!!.getFavorites()
            adapter.currentResults.clear()
            adapter.currentResults.addAll(favoritesArticles)
            activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }
}