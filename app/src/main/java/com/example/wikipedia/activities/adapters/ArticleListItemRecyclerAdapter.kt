package com.example.wikipedia.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.R
import com.example.wikipedia.activities.holders.ListItemHolder
import com.example.wikipedia.activities.models.WikiPage

class ArticleListItemRecyclerAdapter : RecyclerView.Adapter<ListItemHolder>() {

    val currentResults : ArrayList<WikiPage> = ArrayList<WikiPage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val cardItem = LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false)
        return ListItemHolder(cardItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        var page = currentResults[position]
        holder.updatePage(page)
    }
}