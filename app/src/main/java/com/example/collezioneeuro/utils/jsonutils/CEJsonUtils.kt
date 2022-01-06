package com.example.collezioneeuro.utils.jsonutils

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
            val jsonCountries = mapCountriesToJsonCountries(countries)
            val countryJson = mapper.writeValueAsString(jsonCountries)
            Log.println(
                Log.DEBUG,
                "[ExportUserSavedCountries]",
                "[Export] - ${countryJson.subSequence(0, 50)}"
            )
            return countryJson
        }

        /**
         * Mappa le CECountries in JSonCountries, ovvero un oggetto che tiene solo le informazioni
         * che ci servono, non tutti gli attributi di CECountries.
         * Ad esempio, non salviamo:
         *  - drawableID
         *  - drawableUri
         *  - flagUri
         * Ma solo:
         *  - countryTag
         *  - coin value
         *  - coin owned
         */
        private fun mapCountriesToJsonCountries(countries: ArrayList<CECountry>): ArrayList<JsonCountry> {
            val jsonCountries = ArrayList<JsonCountry>()
            for (country in countries) {
                val jsonCoins = ArrayList<JsonCoin>()
                for (coin in country.coins) {
                    val tmpCoin = JsonCoin(coin.value, coin.owned)
                    jsonCoins.add(tmpCoin)
                }
                val tmpJsonCountry = JsonCountry(country.countryTag, jsonCoins)
                jsonCountries.add(tmpJsonCountry)
            }
            return jsonCountries
        }
    }

    data class JsonCountry(val countryTag: String, val coins: ArrayList<JsonCoin>)
    data class JsonCoin(val value: Double, val owned: Boolean)
}