package com.example.collezioneeuro.ui.activity

interface ActionBarActivityInterface {

    /**
     * Imposta il titolo dell'action bar (magari la vuoi come il title del fragment visualizzato)
     */
    fun setActionBarTitle(title: String)

    /**
     * Imposta il sottotitolo dell'action bar (magari la vuoi come il sottotitolo del fragment visualizzato)
     */
    fun setActionBarSubtitle(subtitle: String)

    /**
     * Abilità il navigation drawer (menu di sx) impostando il burger menu come icona e rendendolo apribile
     */
    fun enableDrawer()

    /**
     * Disabilità il navigation drawer togliendo l'icona e non rendendolo apribile
     */
    fun disableDrawer()

    /**
     * Imposta come icona del drawer la freccia all'indietro e ci imposta l'onbackpressed
     */
    fun setBackArrow()

}