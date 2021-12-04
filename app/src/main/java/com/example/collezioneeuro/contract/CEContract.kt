package com.example.collezioneeuro.contract

import com.example.collezioneeuro.model.CECountry

/**
 * Interfacce per mettere in comunicazione le view con il presenter che fornisce i dati dal repository
 */
interface CEContract {

    interface View {
        fun onGetCountries(countries: ArrayList<CECountry>)
    }

    interface Presenter {
        fun bindView(view: View)

        fun getCountries()
    }

}