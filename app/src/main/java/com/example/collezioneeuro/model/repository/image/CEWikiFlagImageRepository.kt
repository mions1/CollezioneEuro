package com.example.collezioneeuro.model.repository.image

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*


class CEWikiFlagImageRepository : CEFlagImageRepositoryInterface {

    companion object {
        private const val baseUrl = "https://it.wikipedia.org/wiki/File:Flag_of_"
    }

    override fun getHtmlUrl(countryEnName: String): String {
        return "$baseUrl${
            countryEnName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
        }.svg"
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

    override fun getImageUrl(document: Document, countryName: String): String? {
        /**
         * div mw-content-text
         *  div file
         *      a ->href
         *          picture coin-cropper
         *              img src="url2euro.jpg o 10cent"
         */

        for (element in document.select("img[src$=.png]")) {
            val html = element.toString()
            Log.println(Log.DEBUG, "[CEECBFlagImageRepo]", "getImageUrl - html: $html")
            if (html.contains(countryName))
                return "https:" + html.subSequence(html.indexOf("/"), html.indexOf(".png") + 4)
                    .toString()
        }
        return null
    }

}