package com.example.collezioneeuro.model.repository.image

import org.jsoup.nodes.Document

interface CEFlagImageRepositoryInterface {

    /**
     * Ritorna l'url della pagina che contiene l'immagine (o le immagini) della bandiera del paese
     */
    fun getHtmlUrl(countryTag: String): String

    /**
     * Ritorna il codice html dell'url passato
     *
     * @param url dell'html che si vuole scaricare
     * @return document relativo all'url passato
     */
    suspend fun getHtml(url: String): Document?

    /**
     * Ritorna l'url dell'immagine rappresentante la bandiera presente dal document (che contiene l'html)
     * relativo all'html del url che contiene l'immagine
     *
     * @param document documento che contiene l'html della pagina contente l'immagine della moneta voluta
     * @param countryName country della banidera della quale si vuole l'immagine
     * @return end-url dell'immagine della moneta voluta
     */
    fun getImageUrl(document: Document, countryName: String): String?

}