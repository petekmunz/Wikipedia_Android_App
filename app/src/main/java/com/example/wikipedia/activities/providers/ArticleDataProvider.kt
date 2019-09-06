package com.example.wikipedia.activities.providers

import android.util.Log
import com.example.wikipedia.activities.models.Urls
import com.example.wikipedia.activities.models.WikiResult
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet

class ArticleDataProvider {

    init {
        FuelManager.instance.baseHeaders = mapOf("User-Agent" to "Local Wiki Kotlin")
    }

    fun search(term: String, skip: Int, take: Int, responseHandler: (result: WikiResult) -> Unit?) {
        Urls.getSearchUrl(term, take, skip).httpGet()
            .responseObject(WikiResult.WikipediaDataDeserializer()) { request, response, result ->
                if (response.statusCode != 200) {
                    throw Exception("Unable to get articles")
                }
                val (data, _) = result
                responseHandler.invoke(data as WikiResult)
            }
    }

    fun getRandom(take: Int, responseHandler: (result: WikiResult) -> Unit?) {
        Urls.getRandomUrl(take).httpGet()
            .responseObject(WikiResult.WikipediaDataDeserializer()) { request, response, result ->
                if (response.statusCode != 200) {
                    Log.d("STATUS_CODE", response.statusCode.toString())
                    throw Exception("Unable to get articles")
                }
                val (data, _) = result
                responseHandler.invoke(data as WikiResult)

            }

    }

}