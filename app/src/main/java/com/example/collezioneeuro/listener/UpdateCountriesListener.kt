package com.example.collezioneeuro.listener

import com.example.collezioneeuro.model.CECountry

/**
 * Interfaccia per l'update della lista delle countries
 * Da estendere se si gestisce tale lista e si vuole rimanere aggiornati sui suoi cambiamenti da parte
 * di altri fragment o activity in tempo reale
 */
interface UpdateCountriesListener {

    /**
     * Callback nel momento in cui la lista delle countries viene modificata
     */
    fun onUpdateCountries(ceCountries: ArrayList<CECountry>)

}