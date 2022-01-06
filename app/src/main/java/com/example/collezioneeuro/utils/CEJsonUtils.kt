package com.example.collezioneeuro.utils

import android.util.Log
import com.example.collezioneeuro.model.CECountry
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class CEJsonUtils {

    companion object {
        private val mapper = jacksonObjectMapper()

        /**
         * Ritorna il json delle countries passate
         */
        fun getJsonCountries(countries: ArrayList<CECountry>): String {
            val countryJson = mapper.writeValueAsString(countries)
            Log.println(
                Log.DEBUG,
                "[ExportUserSavedCountries]",
                "[Export] - ${countryJson.subSequence(0, 50)}"
            )
            return countryJson
        }
    }

}