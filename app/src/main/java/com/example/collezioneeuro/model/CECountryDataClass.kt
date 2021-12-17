package com.example.collezioneeuro.model

import android.os.Parcelable
import com.example.collezioneeuro.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CECountry(
    val country: String,
    val countryTag: String,
    val coins: ArrayList<CECoin>,
    val drawableId: Int?
) :
    Parcelable {
    companion object {
        val countriesTag = arrayListOf<Pair<String, String>>(
            Pair("Andorra", "AD"),
            Pair("Austria", "AT"),
            Pair("Belgio", "BE"),
            Pair("Cipro", "CY"),
            Pair("Città del Vaticano", "VA"),
            Pair("Estonia", "ET"),
            Pair("Finlandia", "FI"),
            Pair("Francia", "FR"),
            Pair("Francia", "AL"),
            Pair("Germania", "DE"),
            Pair("Grecia", "GR"),
            Pair("Irlanda", "IE"),
            Pair("Italia", "IT"),
            Pair("Lettonia", "LV"),
            Pair("Lituania", "LT"),
            Pair("Lussemburgo", "LU"),
            Pair("Malta", "MT"),
            Pair("Paesi Bassi", "NL"),
            Pair("Portogallo", "PT"),
            Pair("Monaco", "MO"),
            Pair("Repubblica di San Marino", "SM"),
            Pair("Slovacchia", "SK"),
            Pair("Slovenia", "SL"),
            Pair("Spagna", "ES"),
        )

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
    }

    fun ownedCount(): Int {
        return coins.count { it.owned }
    }
}

@Parcelize
data class CECoin(
    val value: Double,
    var drawableId: Int?,
    var owned: Boolean,
    var drawableUrl: String?
) : Parcelable