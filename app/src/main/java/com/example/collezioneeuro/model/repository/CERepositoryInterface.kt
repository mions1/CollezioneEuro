package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECountry

interface CERepositoryInterface {

    suspend fun getCountries(): ArrayList<CECountry>

}