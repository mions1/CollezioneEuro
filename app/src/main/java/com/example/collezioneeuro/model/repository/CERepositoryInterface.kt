package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry

interface CERepositoryInterface {

    suspend fun saveCountry(ceCountry: CECountry)
    suspend fun saveCountries(ceCountries: ArrayList<CECountry>)
    suspend fun editCountry(oldCountry: CECountry, newCountry: CECountry)
    suspend fun getCountries(): ArrayList<CECountry>
    suspend fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean)
    suspend fun clear()

}