package com.example.wikipedia.activities

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikipedia.R
import com.example.wikipedia.activities.adapters.ArticleListItemRecyclerAdapter
import com.example.wikipedia.activities.lifecycleUtility.WikiApplication
import com.example.wikipedia.activities.managers.WikiManager
import com.example.wikipedia.activities.models.WikiResult
import com.example.wikipedia.activities.providers.ArticleDataProvider
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private var adapter : ArticleListItemRecyclerAdapter = ArticleListItemRecyclerAdapter()
    private  var wikiManager : WikiManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        wikiManager = (applicationContext as WikiApplication).wikiManager
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        search_results_recycler.layoutManager = LinearLayoutManager(this)
        search_results_recycler.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu!!.findItem(R.id.action_search)
        val searchmanager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchmanager.getSearchableInfo(componentName))
        !searchView.isIconfiedByDefault
        searchView.requestFocus()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    wikiManager?.search(query, 0, 20) { result: WikiResult ->
                        adapter.currentResults.clear()
                        adapter.currentResults.addAll(result.query!!.pages)
                        runOnUiThread { adapter.notifyDataSetChanged() }
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return true
    }
}
