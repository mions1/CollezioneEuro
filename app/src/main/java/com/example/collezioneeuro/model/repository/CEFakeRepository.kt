package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECountry

class CEFakeRepository : CERepositoryInterface {

    companion object {
        val SingleInstance = CEFakeRepository()
    }

    private val countries: ArrayList<CECountry> = ArrayList()

    override suspend fun getCountries(): ArrayList<CECountry> {
        return countries
    }

}