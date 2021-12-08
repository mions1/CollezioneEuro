package com.example.collezioneeuro.model

import android.os.Parcelable
import com.example.collezioneeuro.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CECountry(val country: String, val coins: ArrayList<CECoin>) : Parcelable {
    companion object {
        fun createCoins(): ArrayList<CECoin> {
            val coins = arrayListOf(0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00)
            val ceCoins = ArrayList<CECoin>()
            for (coin in coins)
                ceCoins.add(CECoin(coin, R.drawable.coin_default, false))
            return ceCoins
        }
    }
}

@Parcelize
data class CECoin(val value: Double, var drawableId: Int?, val owned: Boolean) : Parcelable