package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry

interface CERepositoryInterface {

    suspend fun getCountries(): ArrayList<CECountry>
    suspend fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean)

}