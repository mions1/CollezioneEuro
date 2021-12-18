package com.example.collezioneeuro.contract

import org.jsoup.nodes.Document

interface CECoinImageContract {

    interface View {
        fun onGetHtml(document: Document)
    }

    interface Presenter {
        fun bindView(view: View)

        /**
         * Ritorna l'url della pagina che contiene le monete del paese indicato
         */
        fun getHtmlUrl(countryTag: String): String

        /**
         * Ritorna il codice dell'html dell'url indicato
         */
        fun getHtmlCode(url: String)

        /**
         * Ritorna l'url dell'immagine della moneta indicata nel html indicato
         */
        fun getUrlImage(document: Document, value: Double): String?
    }

}