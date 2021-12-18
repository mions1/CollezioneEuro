package com.example.collezioneeuro.contract

import org.jsoup.nodes.Document

interface CEFlagImageContract {

    interface View {
        fun onGetHtml(document: Document, countryName: String)
    }

    interface Presenter {
        fun bindView(view: View)

        /**
         * Ritorna l'url della pagina che contiene la bandiera del paese indicato
         */
        fun getHtmlUrl(countryTag: String): String

        /**
         * Ritorna il codice dell'html dell'url indicato
         */
        fun getHtmlCode(url: String, countryName: String)

        /**
         * Ritorna l'url dell'immagine della bandiera del paese indicato nel html indicato
         */
        fun getUrlImage(document: Document, countryName: String): String?
    }

}