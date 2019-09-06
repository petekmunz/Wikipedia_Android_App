package com.example.wikipedia.activities.models

data class WikiPage (

	var pageid : Int? = null,
	var title : String? = null,
	var thumbnail : WikiThumbnail? = null,
	var fullurl : String? = null

)