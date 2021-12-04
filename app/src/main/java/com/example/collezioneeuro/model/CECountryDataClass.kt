package com.example.collezioneeuro.model

data class CECountry(val country: String, val coins: ArrayList<CECoins>)
data class CECoins(val value: Double, val owned: Boolean)