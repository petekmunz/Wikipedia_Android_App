package com.example.wikipedia.activities.managers

import com.example.wikipedia.activities.models.WikiPage
import com.example.wikipedia.activities.models.WikiResult
import com.example.wikipedia.activities.providers.ArticleDataProvider
import com.example.wikipedia.activities.repositories.FavoritesRepository
import com.example.wikipedia.activities.repositories.HistoryRepository

class WikiManager(
    private val provider: ArticleDataProvider,
    private val favoritesRepository: FavoritesRepository,
    private val historyRepository: HistoryRepository
) {
    private var favoritesCache: ArrayList<WikiPage>? = null
    private var historyCache: ArrayList<WikiPage>? = null

    fun search(term: String, skip: Int, take: Int, handler: (result: WikiResult) -> Unit?) {
        return provider.search(term, skip, take, handler)
    }

    fun getRandom(take: Int, handler: (result: WikiResult) -> Unit?) {
        return provider.getRandom(take, handler)
    }

    fun getHistory(): ArrayList<WikiPage> {
        if (historyCache == null)
            historyCache = historyRepository.getAllHistory()
        return historyCache as ArrayList<WikiPage>
    }

    fun getFavorites() : ArrayList<WikiPage>{
        if(favoritesCache == null)
            favoritesCache = favoritesRepository.getAllFavorites()
        return favoritesCache as ArrayList<WikiPage>
    }

    fun addFavorite(page: WikiPage){
        favoritesCache?.add(page)
        favoritesRepository.addFavorite(page)
    }

    fun removeFavorite(pageID: Int){
        favoritesRepository.removeFavoriteByID(pageID)
        favoritesCache = favoritesCache!!.filter { it.pageid != pageID } as ArrayList<WikiPage>
    }

    fun getIsfavorite(pageID: Int) : Boolean{
        return favoritesRepository.isArticleFavorite(pageID)
    }

    fun addHistory(page: WikiPage){
        historyCache?.add(page)
        historyRepository.addHistory(page)
    }

    fun clearHistory(){
        historyCache?.clear()
        val allHistory = historyRepository.getAllHistory()
        allHistory.forEach { historyRepository.removeHistoryByID(it.pageid!!) }
    }
}