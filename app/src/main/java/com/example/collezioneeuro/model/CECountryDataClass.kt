package com.example.collezioneeuro.model

import android.os.Parcelable
import com.example.collezioneeuro.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CECountry(val country: String, val coins: ArrayList<CECoin>, val drawableId: Int?) :
    Parcelable {
    companion object {
        fun createCoins(): ArrayList<CECoin> {
            val coins = arrayListOf(0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00)
            val ceCoins = ArrayList<CECoin>()
            for (coin in coins)
                ceCoins.add(CECoin(coin, R.drawable.coin_default, false))
            return ceCoins
        }
    }

    fun ownedCount(): Int {
        return coins.count { it.owned }
    }
}

@Parcelize
data class CECoin(val value: Double, var drawableId: Int?, var owned: Boolean) : Parcelable