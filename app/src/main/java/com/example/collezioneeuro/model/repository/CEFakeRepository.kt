package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECountry

class CEFakeRepository : CERepositoryInterface {

    companion object {
        val SingleInstance = CEFakeRepository()
    }

    private val countries: ArrayList<CECountry> = arrayListOf(
        CECountry("Italia", CECountry.createCoins()),
        CECountry("Spagna", CECountry.createCoins()),
        CECountry("Grecia", CECountry.createCoins()),
        CECountry("Germania", CECountry.createCoins()),
        CECountry("Francia", CECountry.createCoins()),
    )

    override suspend fun getCountries(): ArrayList<CECountry> {
        return countries
    }

}