package com.example.wikipedia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.wikipedia.R
import com.example.wikipedia.activities.lifecycleUtility.WikiApplication
import com.example.wikipedia.activities.managers.WikiManager
import com.example.wikipedia.activities.models.WikiPage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_article_detail.*
import org.jetbrains.anko.toast

class ArticleDetailActivity : AppCompatActivity() {

    private var currentPage : WikiPage? = null
    private var wikiManager : WikiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        wikiManager = (applicationContext as WikiApplication).wikiManager
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //get page from extras
        val wikiPageJson = intent.getStringExtra("page")
        currentPage = Gson().fromJson<WikiPage>(wikiPageJson, WikiPage::class.java)
        supportActionBar?.title = currentPage!!.title
        article_detail_webView?.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest? ): Boolean {
                return true
            }
        }

        article_detail_webView.loadUrl(currentPage!!.fullurl)
        wikiManager?.addHistory(currentPage!! )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        } else if (item.itemId == R.id.actionFavorite){
            try {
                //determine if article is already a favorite or not
                if(wikiManager!!.getIsfavorite(currentPage!!.pageid!!)){
                    wikiManager!!.removeFavorite(currentPage!!.pageid!!)
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp)
                    toast("Article removed from Favorites")
                } else{
                    wikiManager!!.addFavorite(currentPage!!)
                    item.setIcon(R.drawable.ic_favorite_white_filled_24dp)
                    toast("Article added to Favorites")
                }
            } catch (e : Exception){
                toast("Unable to update this article")
            }
        }
        return true
    }
}
