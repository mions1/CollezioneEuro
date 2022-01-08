package com.example.collezioneeuro.model

import android.os.Parcelable
import com.example.collezioneeuro.R
import com.example.collezioneeuro.model.CECountryConstants.Companion.countriesTag
import kotlinx.parcelize.Parcelize
import kotlin.math.round

@Parcelize
data class CECountry(
    val country: String,
    val countryTag: String,
    val coins: ArrayList<CECoin>,
    val drawableId: Int? = null,
    val drawableUrl: String? = null
) :
    Parcelable {
    companion object {

        fun createCoins(): ArrayList<CECoin> {
            val coins = arrayListOf(0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00)
            val ceCoins = ArrayList<CECoin>()
            for (coin in coins)
                ceCoins.add(CECoin(coin, R.drawable.coin_default, false, null))
            return ceCoins
        }

        fun getTag(countryName: String): String {
            val find = countriesTag.find { it.first == countryName }
            if (find != null)
                return find.second
            return ""
        }

        /**
         * Restituisce la country, trovandola tramite il tag, nella lista passata
         */
        fun getCountryByTag(ceCountries: ArrayList<CECountry>, tag: String): CECountry? {
            return ceCountries.find { it.countryTag == tag }
        }

    }

    fun getTotal(): Double {
        return round((getOwnedList().sumOf { it.value }) * 100) / 100
    }

    /**
     * Restituisce la lista delle sole monete possedute della country
     */
    fun getOwnedList(): ArrayList<CECoin> {
        val ownedList = ArrayList<CECoin>()
        coins.forEach { if (it.owned) ownedList.add(it) }
        return ownedList
    }

    fun ownedCount(): Int {
        return coins.count { it.owned }
    }

    fun getCoinByValue(value: Double): CECoin? {
        return coins.find { it.value == value }
    }

}

@Parcelize
data class CECoin(
    val value: Double,
    var drawableId: Int? = null,
    var owned: Boolean,
    var drawableUrl: String? = null
) : Parcelable