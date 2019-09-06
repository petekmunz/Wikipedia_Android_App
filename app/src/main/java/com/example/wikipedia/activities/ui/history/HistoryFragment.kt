package com.example.wikipedia.activities.ui.history

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.R
import com.example.wikipedia.activities.adapters.ArticleListItemRecyclerAdapter
import com.example.wikipedia.activities.lifecycleUtility.WikiApplication
import com.example.wikipedia.activities.managers.WikiManager
import org.jetbrains.anko.*

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private var historyRecycler : RecyclerView? = null
    private var wikiManager : WikiManager? = null
    private val adapter = ArticleListItemRecyclerAdapter()

    init {
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel =
            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        historyRecycler = root.findViewById(R.id.history_article_recycler)

        historyRecycler!!.layoutManager = LinearLayoutManager(context)
        historyRecycler!!.adapter = adapter
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!!.itemId == R.id.actionHistory){
            //show confirmation alert
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure you want to delete your history?")
            builder.setPositiveButton("Yes"){dialogInterface, i ->
                //User affirms
                //clear history
                adapter.currentResults.clear()
                doAsync {
                    wikiManager!!.clearHistory()
                }
                activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
            }
            builder.setNegativeButton("No"){dialogInterface, i ->
                //if needed handle here
            }
            builder.show()

        }
        return true
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val historyArticles = wikiManager!!.getHistory()
            adapter.currentResults.clear()
            adapter.currentResults.addAll(historyArticles)
            activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }
}