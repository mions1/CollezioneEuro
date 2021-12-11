package com.example.collezioneeuro.contract

import com.example.collezioneeuro.model.CECoin
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

        fun saveCountry(ceCountry: CECountry)
        fun saveCountries(ceCountries: ArrayList<CECountry>)
        fun getCountries()
        fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean)
        fun clear()
    }

}