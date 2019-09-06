package com.example.wikipedia.activities.lifecycleUtility

import android.app.Application
import com.example.wikipedia.activities.managers.WikiManager
import com.example.wikipedia.activities.providers.ArticleDataProvider
import com.example.wikipedia.activities.repositories.ArticleDatabaseOpenHelper
import com.example.wikipedia.activities.repositories.FavoritesRepository
import com.example.wikipedia.activities.repositories.HistoryRepository

class WikiApplication : Application() {
    private var dbHeper : ArticleDatabaseOpenHelper? = null
    private var favoritesRepository : FavoritesRepository? = null
    private  var historyRepository : HistoryRepository? = null
    private  var wikiProvider : ArticleDataProvider? = null
    var wikiManager : WikiManager? = null
        private set

    override fun onCreate() {
        super.onCreate()

        dbHeper = ArticleDatabaseOpenHelper(applicationContext)
        favoritesRepository = FavoritesRepository(dbHeper!!)
        historyRepository = HistoryRepository(dbHeper!!)
        wikiProvider = ArticleDataProvider()
        wikiManager = WikiManager(wikiProvider!!, favoritesRepository!!, historyRepository!!)
    }
}