package com.example.collezioneeuro.model.repository.image

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


class CEECBCoinImageRepository : CECoinImageRepositoryInterface {

    companion object {
        private const val baseUrl = "https://www.ecb.europa.eu"
        private const val baseUrlHtmlCoins = "$baseUrl/euro/coins/html"
    }

    override fun getHtmlUrl(countryTag: String): String {
        return "$baseUrlHtmlCoins/$countryTag.$countryTag.html".lowercase()
    }

    override suspend fun getHtml(url: String): Document? {
        var doc: Document? = null
        try {
            doc = Jsoup.connect(url).get()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return doc
    }

    override fun getImageUrl(document: Document, value: Double): String? {
        /**
         * div boxes
         *  div box
         *      div coins loaded
         *          picture coin-cropper
         *              img src="url2euro.jpg o 10cent"
         */
        var valueToString = ""
        when (value) {
            0.01 -> valueToString = "1cent"
            0.02 -> valueToString = "2cent"
            0.05 -> valueToString = "5cent"
            0.10 -> valueToString = "10cent"
            0.20 -> valueToString = "20cent"
            0.50 -> valueToString = "50cent"
            1.00 -> valueToString = "1euro"
            2.00 -> valueToString = "2euro"
        }
        Log.println(Log.DEBUG, "[CEECBImageRepo]", "getImageUrl - valueToString: $valueToString")

        for (element in document.select("img[src$=.jpg]")) {
            val html = element.toString()
            Log.println(Log.DEBUG, "[CEECBImageRepo]", "getImageUrl - html: $html")
            if (html.contains(valueToString))
                return baseUrl + html.subSequence(html.indexOf("/"), html.indexOf(".jpg") + 4)
                    .toString()
        }
        return null
    }

}