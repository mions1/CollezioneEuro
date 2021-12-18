package com.example.collezioneeuro.model

import android.os.Parcelable
import com.example.collezioneeuro.R
import kotlinx.parcelize.Parcelize

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
        val countriesTag = arrayListOf(
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

        val countriesEnglish = arrayListOf<Pair<String, String>>(
            Pair("Andorra", "Andorra"),
            Pair("Austria", "Austria"),
            Pair("Belgio", "Belgium"),
            Pair("Cipro", "Cyprus"),
            Pair("Città del Vaticano", "the Vatican City"),
            Pair("Estonia", "Estonia"),
            Pair("Finlandia", "Finland"),
            Pair("Francia", "France"),
            Pair("Germania", "Germany"),
            Pair("Grecia", "Greece"),
            Pair("Irlanda", "Ireland"),
            Pair("Italia", "Italy"),
            Pair("Lettonia", "Latvia"),
            Pair("Lituania", "Lithuania"),
            Pair("Lussemburgo", "Luxembourg"),
            Pair("Malta", "Malta"),
            Pair("Paesi Bassi", "Netherlands"),
            Pair("Portogallo", "Portugal"),
            Pair("Monaco", "Monaco"),
            Pair("Repubblica di San Marino", "San Marino"),
            Pair("Slovacchia", "Slovakia"),
            Pair("Slovenia", "Slovenia"),
            Pair("Spagna", "Spain")
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
    var drawableId: Int? = null,
    var owned: Boolean,
    var drawableUrl: String? = null
) : Parcelable