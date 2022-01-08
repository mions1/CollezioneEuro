package com.example.collezioneeuro.utils.jsonutils

import com.example.collezioneeuro.model.CECountry

interface CEJsonUtilsContract {

    interface View {
        fun onGetJson(json: String)
        fun onReadJson(ceCountries: ArrayList<CECountry>)
    }

    interface Presenter {
        fun bindView(view: View)

        fun getJsonCountries(countries: ArrayList<CECountry>)
        fun readCountryJson(countryJson: String)
    }

}