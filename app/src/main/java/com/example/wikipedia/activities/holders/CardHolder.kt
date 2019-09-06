package com.example.wikipedia.activities.holders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.wikipedia.R
import com.example.wikipedia.activities.ArticleDetailActivity
import com.example.wikipedia.activities.models.WikiPage
import com.google.gson.Gson

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val articleImageView : ImageView = itemView.findViewById(R.id.article_image)
    private val titleTextView : TextView = itemView.findViewById(R.id.article_title)

    private var currentPage : WikiPage? = null

    init {
        itemView.setOnClickListener {  view: View? ->
            var detailPageintent = Intent(itemView.context, ArticleDetailActivity::class.java)
            var pageJson = Gson().toJson(currentPage)
            detailPageintent.putExtra("page", pageJson)
            itemView.context.startActivity(detailPageintent)
        }
    }

    fun updatePage(page: WikiPage){
        currentPage = page

        titleTextView.text = page.title
        //load image
        if(page.thumbnail != null){
            Glide.with(itemView.context)
                .load(page.thumbnail!!.source)
                .override(Target.SIZE_ORIGINAL)
                //.placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_image_black_24dp)
                .into(articleImageView)
        }
    }
}