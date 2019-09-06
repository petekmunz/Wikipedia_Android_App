package com.example.wikipedia.activities.repositories

import com.example.wikipedia.activities.models.WikiPage
import com.example.wikipedia.activities.models.WikiThumbnail
import com.google.gson.Gson
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

class FavoritesRepository(val databaseHelper : ArticleDatabaseOpenHelper) {
    private val TABLE_NAME : String = "Favorites"

    fun addFavorite(page: WikiPage){
        databaseHelper.use {
            insert(TABLE_NAME,
                "id" to page.pageid,
                "title" to page.title,
                "url" to page.fullurl,
                "thumbnailJson" to Gson().toJson(page.thumbnail))
        }
    }

    fun removeFavoriteByID(pageID : Int){
        databaseHelper.use {
            delete(TABLE_NAME, "id = {pageid}", "pageid" to pageID)
        }
    }

    fun isArticleFavorite(pageID: Int) : Boolean{
        //get favorites and filter
        var pages = getAllFavorites()
        return pages.any { wikipage ->
            wikipage.pageid == pageID
        }

    }

    fun getAllFavorites() : ArrayList<WikiPage>{
        var pages = ArrayList<WikiPage>()

        val articleRowParser = rowParser { id: Int, title: String, url: String, thumbnailjson: String ->
            val page = WikiPage()
            page.pageid = id
            page.title = title
            page.fullurl = url
            page.thumbnail = Gson().fromJson(thumbnailjson, WikiThumbnail ::class.java)

            pages.add(page)
        }
        databaseHelper.use {
            select(TABLE_NAME).parseList(articleRowParser)
        }

        return pages
    }
}