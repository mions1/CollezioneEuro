package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.R
import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry

class CEFakeRepository : CERepositoryInterface {

    companion object {
        val SingleInstance = CEFakeRepository()
    }

    private val countries: ArrayList<CECountry> = arrayListOf(
        CECountry("Italia", createItalianCoins()),
        CECountry("Spagna", CECountry.createCoins()),
        CECountry("Grecia", CECountry.createCoins()),
        CECountry("Germania", CECountry.createCoins()),
        CECountry("Francia", CECountry.createCoins()),
    )

    private fun createItalianCoins(): ArrayList<CECoin> {
        val coins = CECountry.createCoins()
        coins[0].drawableId = R.drawable.italia_001
        coins[0].owned = true
        coins[1].drawableId = R.drawable.italia_002
        coins[1].owned = true
        coins[2].drawableId = R.drawable.italia_005
        coins[3].drawableId = R.drawable.italia_010
        coins[3].owned = true
        coins[4].drawableId = R.drawable.italia_020
        coins[5].drawableId = R.drawable.italia_050
        coins[6].drawableId = R.drawable.italia_100
        coins[6].owned = true
        coins[7].drawableId = R.drawable.italia_200
        return coins
    }

    override suspend fun getCountries(): ArrayList<CECountry> {
        return countries
    }

    override suspend fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean) {
        val ceCoinIdx = ceCountry.coins.indexOf(ceCoin)
        val ceCountryIdx = countries.indexOf(ceCountry)
        countries[ceCountryIdx].coins[ceCoinIdx].owned = owned
    }

}