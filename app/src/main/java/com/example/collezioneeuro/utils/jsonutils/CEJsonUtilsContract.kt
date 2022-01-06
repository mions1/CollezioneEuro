package com.example.collezioneeuro.utils.jsonutils

import com.example.collezioneeuro.model.CECountry

interface CEJsonUtilsContract {

    interface View {
        fun onGetJson(json: String)
    }

    interface Presenter {
        fun bindView(view: View)

        fun getJsonCountries(countries: ArrayList<CECountry>)
    }

}