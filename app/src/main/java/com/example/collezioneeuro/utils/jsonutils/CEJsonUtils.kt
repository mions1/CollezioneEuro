package com.example.collezioneeuro.utils.jsonutils

import android.util.Log
import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

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
         * Leggo il json e restituisco la lista delle countries del json appena letto
         */
        fun readCountryJson(countryJson: String): ArrayList<CECountry> {
            val countries = mapper.readValue<ArrayList<JsonCountry>>(countryJson)
            val ceCountries = mapJsonCountriesToCountries(countries)
            Log.println(
                Log.DEBUG,
                "[ExportUserSavedCountries]",
                "[Export] - $ceCountries"
            )
            return ceCountries
        }

        /**
         * Mappa le JSonCountries in CECountries. Quindi, alle jsoncountries che tengono solo alcune
         * informazioni, aggiungo le restanti prese dal repo
         * Ad esempio, mi serve prendere:
         *  - drawableID
         *  - drawableUri
         *  - flagUri
         */
        private fun mapJsonCountriesToCountries(jsonCountries: ArrayList<JsonCountry>): ArrayList<CECountry> {
            val tmpCECountries = CEFakeRepository.getOriginalCountries()
            val ceCountries = ArrayList<CECountry>()
            for (jsonCountry in jsonCountries) {
                val ceCoins = ArrayList<CECoin>()
                val tmpCECountry = CECountry.getCountryByTag(tmpCECountries, jsonCountry.countryTag)
                for (coin in jsonCountry.coins) {
                    val tmpCoin = tmpCECountry?.getCoinByValue(coin.value)
                    tmpCoin?.owned = coin.owned
                    tmpCoin?.let { ceCoins.add(it) }
                }
                tmpCECountry?.coins?.clear()
                tmpCECountry?.coins?.addAll(ceCoins)
                tmpCECountry?.let { ceCountries.add(it) }
            }
            return ceCountries
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