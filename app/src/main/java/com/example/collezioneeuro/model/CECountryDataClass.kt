package com.example.collezioneeuro.model

import android.graphics.drawable.Drawable

data class CECountry(val country: String, val coins: ArrayList<CECoin>) {
    companion object {
        fun createCoins(): ArrayList<CECoin> {
            val coins = arrayListOf(0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00)
            val ceCoins = ArrayList<CECoin>()
            for (coin in coins)
                ceCoins.add(CECoin(coin, null, false))
            return ceCoins
        }
    }
}

data class CECoin(val value: Double, var image: Drawable?, val owned: Boolean)