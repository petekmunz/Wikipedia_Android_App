package com.example.wikipedia.activities.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Reader

data class WikiResult (
	val query : WikiQueryData
){
	class WikipediaDataDeserializer : ResponseDeserializable<WikiResult> {
		override fun deserialize(reader: Reader): WikiResult? {
			return Gson().fromJson(reader, WikiResult::class.java)
		}
	}
}
